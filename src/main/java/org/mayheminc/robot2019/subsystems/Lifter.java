package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.robot2019.RobotMap;

public class Lifter extends Subsystem {

    // constants for power
    private static final double STOP_POWER = 0.0;
    private static final double LIFTING_POWER = 0.7; // used 0.7 for comp robot; used 1.0 for initial testing; reducing
                                                     // power to save mechanism

    // constants for positions
    private static final int STARTING_POS = 0;
    public static final int LIFTED_POS = 950000; // determined empirically on bag night; was 900000; increased to 950000
                                                 // at PineTree

    private static final int EXTRA_FOR_RAISED_POS = 150000;
    public static final int RAISED_AFTER_LIFTED = LIFTED_POS + EXTRA_FOR_RAISED_POS;

    private static final int IN_POSITION_SLOP = 100;
    private static final int MAX_MOTOR_OFFSET = 200000;

    private static final double K_FACTOR = 1.0 / MAX_MOTOR_OFFSET;

    private final MayhemTalonSRX motorLeftA = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRightA = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    private final MayhemTalonSRX motorLeftB = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_B_TALON);
    private final MayhemTalonSRX motorRightB = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_B_TALON);

    private int m_desiredPosition;
    private double m_targetSpeed;
    private double m_modifier = 0.0;

    private enum LifterMode {
        OFF, MANUAL, SYNC_LIFT
    };

    private LifterMode m_mode = LifterMode.OFF; // start with Lifter OFF

    public Lifter() {

        // Config right motor
        configMotor(motorLeftA, false);
        configMotor(motorRightA, true);

        configMotorFollower(motorLeftB, motorLeftA, false);
        configMotorFollower(motorRightB, motorRightA, true);

        // ensure the lifter is constructed in the OFF mode
        m_mode = LifterMode.OFF;

        // Tuck();
    }

    /**
     * Set a motor to follow another motor
     */
    private void configMotorFollower(MayhemTalonSRX follower, MayhemTalonSRX following, boolean b) {
        configMotor(follower, b);
        follower.changeControlMode(ControlMode.Follower);
        follower.set(following.getDeviceID());
    }

    private void configMotor(MayhemTalonSRX motor, boolean inverted) {
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.configClosedloopRamp(0.1);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        motor.setInverted(inverted);
    }

    public void zero() {
        motorLeftA.setSelectedSensorPosition(STARTING_POS);
        motorRightA.setSelectedSensorPosition(STARTING_POS);

        stop();

        SmartDashboard.putString("Lifter Debug", "Zero");
    }

    public void update() {
        // Stop if not being commanded to move
        if (Math.abs(m_targetSpeed) < 0.05) {
            stop();
        }

        // provide for manual control of lifter for debugging.
        // if (Robot.oi.getDriverStickY() != 0.0) {
        // setManual(Robot.oi.getDriverStickY());
        // }

        switch (m_mode) {
        case OFF:
            SmartDashboard.putString("Lifter Mode", "OFF");
            stop();
            break;
        case MANUAL:
            SmartDashboard.putString("Lifter Mode", "MANUAL");
            motorSet(m_targetSpeed);
            break;
        case SYNC_LIFT:
            SmartDashboard.putString("Lifter Mode", "ACTIVELY_LIFTING");

            // Get the encoder postions
            int pos_r = motorRightA.getSelectedSensorPosition();
            int pos_l = motorLeftA.getSelectedSensorPosition();

            int delta = pos_l - pos_r; // positive delta means L is "ahead"
            m_modifier = delta * K_FACTOR; // scale delta by the K_FACTOR
            // constrain modifier to be in the range -1.0 < m_modifier < 1.0
            if (m_modifier > 1.0) {
                m_modifier = 1.0;
            } else if (m_modifier < -1.0) {
                m_modifier = -1.0;
            }

            // Stop if done climbing or done tucking
            if ((m_targetSpeed > 0 && pos_r >= m_desiredPosition - Lifter.IN_POSITION_SLOP)
                    || (m_targetSpeed < 0 && pos_r <= m_desiredPosition + Lifter.IN_POSITION_SLOP)) {
                SmartDashboard.putString("Lifter Debug", "Done");
                stop();
            }

            // if the positions are too far apart, emergency stop.
            else if (Math.abs(delta) > Lifter.MAX_MOTOR_OFFSET) {
                SmartDashboard.putString("Lifter Debug", "E-Stop");
                stop();
            }

            // otherwise, command the two motors to the m_targetSpeed, adjusted
            // proportionally to the difference
            else {
                // should really probably just have the motor set commands be the same, but
                // modified by a proportional difference
                SmartDashboard.putString("Lifter Debug", "Matched");

                // enhance right power by the modifier and "dehance" left power by the modifier
                // (since positive modifier indicates that left is ahead)
                motorRightA.set(ControlMode.PercentOutput, m_targetSpeed * (1.0 + m_modifier));
                motorLeftA.set(ControlMode.PercentOutput, m_targetSpeed * (1.0 - m_modifier));
            }
            break;
        }
    }

    public void setManual(double manualPower) {
        setTargetMotorSpeed(manualPower);
        m_mode = LifterMode.MANUAL;
        m_desiredPosition = 0;
    }

    public void setPositionWithPower(int position, double power) {
        setTargetMotorSpeed(power);
        m_mode = LifterMode.SYNC_LIFT;
        m_desiredPosition = position;
    }

    public void Lift(int desiredPosition) {
        // Received a command to lift. If we're already "lifted" don't do anything!
        m_desiredPosition = desiredPosition;
        if (!IsAtSetpoint()) {
            // Set parameters so that "update()" will climb
            setTargetMotorSpeed(Lifter.LIFTING_POWER);
            m_mode = LifterMode.SYNC_LIFT;
        }
    }

    public void stop() {
        motorSet(Lifter.STOP_POWER);
        m_mode = LifterMode.OFF;
        m_desiredPosition = motorLeftA.getSelectedSensorPosition();
    }

    private void setTargetMotorSpeed(double value) {
        m_targetSpeed = value;
    }

    private void motorSet(double value) {
        m_targetSpeed = value;
        motorLeftA.set(ControlMode.PercentOutput, value);
        motorRightA.set(ControlMode.PercentOutput, value);
    }

    public boolean IsAtSetpoint() {
        // TODO: Need to fix this to handle cases where we "jump" past the desired
        // position too far.
        // KBS implemented a temporary "hack" that only works in the upward direction.
        // return Math.abs(motorLeft.getPosition() - m_pos) < Lifter.IN_POSITION_SLOP;
        return motorLeftA.getPosition() - m_desiredPosition > -Lifter.IN_POSITION_SLOP;
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Lifter Modifier", m_modifier);
        SmartDashboard.putNumber("Lifter Pos R", motorRightA.getSelectedSensorPosition());
        SmartDashboard.putNumber("Lifter Pos L", motorLeftA.getSelectedSensorPosition());
        SmartDashboard.putNumber("Lifter VBus R", motorRightA.getMotorOutputPercent());
        SmartDashboard.putNumber("Lifter VBus L", motorLeftA.getMotorOutputPercent());
        SmartDashboard.putNumber("Lifter Stick", Robot.oi.getDriverStickY());
    }

}
