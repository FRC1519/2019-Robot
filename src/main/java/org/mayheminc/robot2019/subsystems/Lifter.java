package org.mayheminc.robot2019.subsystems;

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
    private static final double TUCKED_POWER = -0.1;
    private static final double LIFTING_POWER = 0.2;   // used 0.7 for comp robot; used 1.0 for initial testing;  reducing power to save mechanism

    private static final double SLOW_SPEED_MULTIPLIER = 0.8;

    // constants for positions
    private static final int STARTING_POS = 0;
    private static final int LIFTED_POS = 800000;           // determined empirically on bag night
    public static final int AUTO_LIFTED_POS_1 = 100000;     // 100k ticks debug tick count
    public static final int AUTO_LIFTED_POS_2 = 200000;     // 200k ticks debug tick count

    private static final int IN_POSITION_SLOP = 100;
    private static final int MAX_MOTOR_OFFSET = 200000;
    private static final int NOMINAL_MOTOR_OFFSET = 50000;

    private final MayhemTalonSRX motorLeftA = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRightA = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    private final MayhemTalonSRX motorLeftB = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_B_TALON);
    private final MayhemTalonSRX motorRightB = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_B_TALON);

    private int m_desiredPosition;
    private double m_targetSpeed;

    private Boolean m_activelyLifting = false;

    public Lifter() {

        // Config right motor
        configMotor(motorLeftA, false);
        configMotor(motorRightA, true);

        configMotorFollower(motorLeftB, motorLeftA, false);
        configMotorFollower(motorRightB, motorRightA, true);

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
        motorLeftA.setSelectedSensorPosition(0); // start at 0
        motorRightA.setSelectedSensorPosition(0); // start at 0

        stop();

        SmartDashboard.putString("Lifter Debug", "Zero");

    }

    public void synchronizedLift() {

        // Stop if not being commanded to move
        if ((-0.05 < m_targetSpeed) && (m_targetSpeed < 0.05)) {
            stop();
        }

        if (this.m_activelyLifting) {
            // Get the encoder postions
            int pos_r = motorRightA.getSelectedSensorPosition();
            int pos_l = motorLeftA.getSelectedSensorPosition();

            // Stop if done climbing or done tucking
            if ((m_targetSpeed > 0 && pos_r >= m_desiredPosition - Lifter.IN_POSITION_SLOP)
                    || (m_targetSpeed < 0 && pos_r <= m_desiredPosition + Lifter.IN_POSITION_SLOP)) {
                SmartDashboard.putString("Lifter Debug", "Done");
                stop();
            }

            // if the positions are too far apart, emergency stop.
            else if (Math.abs(pos_r - pos_l) > Lifter.MAX_MOTOR_OFFSET) {
                SmartDashboard.putString("Lifter Debug", "E-Stop");
                stop();
            }

            // if the positions are close together, then lift together.
            else if (Math.abs(pos_r - pos_l) < Lifter.NOMINAL_MOTOR_OFFSET) {
                SmartDashboard.putString("Lifter Debug", "Matched");
                motorSet(m_targetSpeed);
            }

            // If one motor is far ahead of the other, slow down 1 side. Depends on the
            // direction of the target speed.
            else if ((m_targetSpeed > 0 && pos_r > pos_l) || (m_targetSpeed < 0 && pos_l > pos_r)) {
                SmartDashboard.putString("Lifter Debug", "Slow R");
                motorRightA.set(ControlMode.PercentOutput, m_targetSpeed * SLOW_SPEED_MULTIPLIER);
                motorLeftA.set(ControlMode.PercentOutput, m_targetSpeed);
            }

            else if ((m_targetSpeed > 0 && pos_r < pos_l) || (m_targetSpeed < 0 && pos_l < pos_r)) {
                SmartDashboard.putString("Lifter Debug", "Slow L");
                motorLeftA.set(ControlMode.PercentOutput, m_targetSpeed * SLOW_SPEED_MULTIPLIER);
                motorRightA.set(ControlMode.PercentOutput, m_targetSpeed);
            }

        } else {  // not actively lifting -- tell the lifter to stop
            stop();
        }
    }

    public void set(int position) {
        setTargetMotorSpeed(Lifter.LIFTING_POWER);
        this.m_activelyLifting = true;
        m_desiredPosition = position;
    }

    public void Lift() {
        // Received a command to lift.  If we're already "lifted" don't do anything!
        m_desiredPosition = Lifter.LIFTED_POS;
        if (!IsAtSetpoint()) { 
            // Tell synchronizedLift to climb
            setTargetMotorSpeed(Lifter.LIFTING_POWER);
            this.m_activelyLifting = true;
        }

    }

    public void Tuck() {
        // start with percent output to keep the lifter tucked under the belly.
        setTargetMotorSpeed(Lifter.LIFTING_POWER);
        this.m_activelyLifting = true;
        m_desiredPosition = Lifter.STARTING_POS;
    }

    public void stop() {
        motorSet(Lifter.STOP_POWER);
        this.m_activelyLifting = false;
        m_desiredPosition = motorLeftA.getSelectedSensorPosition();
    }

    private void setTargetMotorSpeed (double value) {
        m_targetSpeed = value;
    }

    private void motorSet(double value) {
        m_targetSpeed = value;
        motorLeftA.set(ControlMode.PercentOutput, value);
        motorRightA.set(ControlMode.PercentOutput, value);
    }

    public boolean IsAtSetpoint() {
        // TODO:  Need to fix this to handle cases where we "jump" past the desired position too far.
        // KBS implemented a temporary "hack" that only works in the upward direction.
        // return Math.abs(motorLeft.getPosition() - m_pos) < Lifter.IN_POSITION_SLOP;
        return motorLeftA.getPosition() - m_desiredPosition > -Lifter.IN_POSITION_SLOP;
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Lifter Pos R", motorRightA.getSelectedSensorPosition());
        SmartDashboard.putNumber("Lifter Pos L", motorLeftA.getSelectedSensorPosition());
    }

}
