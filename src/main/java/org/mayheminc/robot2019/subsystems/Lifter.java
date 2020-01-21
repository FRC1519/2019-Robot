package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.robot2019.RobotMap;

public class Lifter extends SubsystemBase {

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

    private static final int IN_POSITION_SLOP = 20000;
    private static final int MAX_MOTOR_OFFSET = 200000;

    private static final double K_FACTOR = 1.0 / MAX_MOTOR_OFFSET;

    private final MayhemTalonSRX motorLeftA = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRightA = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    private final MayhemTalonSRX motorLeftB = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_B_TALON);
    private final MayhemTalonSRX motorRightB = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_B_TALON);

    private int m_desiredPosition;
    private int m_direction;
    private double m_targetSpeed;
    private double m_modifier = 0.0;

    private enum LifterMode {
        OFF, MANUAL, SYNC_LIFT, MOTION_MAGIC_POSITION
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
        motor.configPeakOutputVoltage(+12.0 * .7, -12.0 * .7); // set peak output voltage to 70%
        motor.configClosedloopRamp(0.1);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        motor.setInverted(inverted);

        motor.configForwardSoftLimitEnable(false);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        motor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        // initial calcs for computing kP...
        // If we want 70% power 200000 ticks from target (this is about 20% from a full
        // lift),
        // kP = (0.70 * 1023) / 200000 = ~.004
        motor.config_kP(0, 0.01, 0); // based upon Robert's initial calcs, above

        // typical value of about 1/100 of kP for starting tuning
        motor.config_kI(0, 0.0, 0);

        // typical value of about 10x to 100x of kP for starting tuning
        motor.config_kD(0, 0.0, 0);

        // practically always set kF to 0 for position control
        // for things like gravity compensation, use the "arbitrary feed forward" that
        // can be specified with the "4-parameter" TalonSRX.set() method
        motor.config_kF(0, 0.0, 0);

        // The total lift takes about 1Million ticks.
        // The cruise should be about 80% of the travel or 800k ticks
        // The lift should take about 3 seconds.
        // the velocity is 800k ticks / 30 (100ms) = 26666 ticks per 100ms
        motor.configMotionCruiseVelocity(26666);
        // acceleration of 2x velocity allows cruise to be attained in 1/2 second
        motor.configMotionAcceleration(26666 * 2);
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
        case MOTION_MAGIC_POSITION:
            motorRightA.set(ControlMode.Position, m_desiredPosition);
            motorLeftA.set(ControlMode.Position, m_desiredPosition);// MotionMagic
            break;
        }
    }

    public void setManual(double manualPower) {
        setTargetMotorSpeed(manualPower);
        m_mode = LifterMode.MANUAL;
        m_desiredPosition = 0;
        m_direction = 0;
    }

    public void setPositionWithPower(int position, double power) {
        setTargetMotorSpeed(power);
        m_mode = LifterMode.SYNC_LIFT;
        m_desiredPosition = position;

        setDirectionFlag();
    }

    public void setPosition(int position) {
        m_mode = LifterMode.MOTION_MAGIC_POSITION;
        m_desiredPosition = position;
        setTargetMotorSpeed(0.2);

        setDirectionFlag();
    }

    private void setDirectionFlag() {
        int pos_r = motorRightA.getSelectedSensorPosition();
        int pos_l = motorLeftA.getSelectedSensorPosition();
        int posAvg = (pos_r + pos_l) / 2;

        // if the desired position is above the current position, the direction is +1,
        // otherwise -1
        m_direction = (m_desiredPosition > posAvg) ? 1 : -1;
    }

    public void Lift(int desiredPosition) {
        // Received a command to lift. If we're already "lifted" don't do anything!
        m_desiredPosition = desiredPosition;

        setDirectionFlag();

        if (!isAtSetpoint()) {
            // Set parameters so that "update()" will climb
            setTargetMotorSpeed(Lifter.LIFTING_POWER);
            m_mode = LifterMode.SYNC_LIFT;
        }
    }

    public void stop() {
        motorSet(Lifter.STOP_POWER);
        m_mode = LifterMode.OFF;
        m_desiredPosition = motorLeftA.getSelectedSensorPosition();
        m_direction = 0;
    }

    private void setTargetMotorSpeed(double value) {
        m_targetSpeed = value;
    }

    private void motorSet(double value) {
        m_targetSpeed = value;
        motorLeftA.set(ControlMode.PercentOutput, value);
        motorRightA.set(ControlMode.PercentOutput, value);
    }

    public boolean isAtSetpoint() {
        // Look for when we have reached the setpoint; need to consider direction of the
        // most recent movement to allow "overshoot" to still result in a "true" retval.
        switch (m_direction) {
        case 0:
        default:
            return true;
        case 1:
            return motorLeftA.getPosition() - m_desiredPosition > -Lifter.IN_POSITION_SLOP;
        case -1:
            return motorLeftA.getPosition() - m_desiredPosition < Lifter.IN_POSITION_SLOP;
        }
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
