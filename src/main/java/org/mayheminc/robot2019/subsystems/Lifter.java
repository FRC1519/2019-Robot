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
    // private static final double LIFTING_POWER = 1.0;
    private static final double LIFTING_POWER = 0.4; // debug speed

    private static final double SLOW_SPEED_MULTIPLIER = 0.8;

    // constants for positions
    private static final int STARTING_POS = 0;
    // private static final int LIFTED_POS = 1000000; // 1 million ticks
    private static final int LIFTED_POS = 500000; // 100k ticks debug tick count
    public static final int AUTO_LIFTED_POS_1 = 100000; // 100k ticks debug tick count
    public static final int AUTO_LIFTED_POS_2 = 200000; // 200k ticks debug tick count

    private static final int IN_POSITION_SLOP = 100;
    private static final int MAX_MOTOR_OFFSET = 200000;
    private static final int NOMINAL_MOTOR_OFFSET = 50000;

    private final MayhemTalonSRX motorLeft = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRight = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    private final MayhemTalonSRX motorLeftB = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_B_TALON);
    private final MayhemTalonSRX motorRightB = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_B_TALON);

    private int m_pos;
    private double m_targetSpeed;

    private Boolean StartClimb = false;

    public Lifter() {

        // Config right motor
        ConfigMotor(motorLeft, false);
        ConfigMotor(motorRight, true);

        ConfigMotorFollower(motorLeftB, motorLeft, false);
        ConfigMotorFollower(motorRightB, motorRight, true);

        // Tuck();
    }

    /**
     * Set a motor to follow another motor
     */
    private void ConfigMotorFollower(MayhemTalonSRX follower, MayhemTalonSRX following, boolean b) {
        follower.changeControlMode(ControlMode.Follower);
        follower.set(following.getDeviceID());
        follower.setInverted(b);
    }

    private void ConfigMotor(MayhemTalonSRX motor, boolean inverted) {
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        motor.setInverted(inverted);
    }

    public void Zero() {
        motorLeft.setSelectedSensorPosition(0); // start at 0
        motorRight.setSelectedSensorPosition(0); // start at 0

        // motorLeftB.setSelectedSensorPosition(0); // start at 0
        // motorRightB.setSelectedSensorPosition(0); // start at 0

        Stop();

        SmartDashboard.putString("Lifter Debug", "Zero");

    }

    public void AutoLift() {
        // Get the encoder postions
        if (this.StartClimb) {
            int pos_r = motorRight.getSelectedSensorPosition();
            int pos_l = motorLeft.getSelectedSensorPosition();

            // Stop if done climbing or done tucking
            if ((m_targetSpeed > 0 && pos_r >= m_pos - Lifter.IN_POSITION_SLOP)
                    || (m_targetSpeed < 0 && pos_r <= m_pos + Lifter.IN_POSITION_SLOP)) {
                SmartDashboard.putString("Lifter Debug", "Done");
                Stop();
            }

            // // if the positions are too far apart, emergency stop.
            // else if (Math.abs(pos_r - pos_l) > Lifter.MAX_MOTOR_OFFSET) {
            // SmartDashboard.putString("Lifter Debug", "E-Stop");
            // Stop();
            // }

            // if the positions are close together, then lift together.
            else if (Math.abs(pos_r - pos_l) < Lifter.NOMINAL_MOTOR_OFFSET) {
                SmartDashboard.putString("Lifter Debug", "Matched");
                motorSet(m_targetSpeed);
            }

            // If one motor is far ahead of the other, slow down 1 side. Depends on the
            // direction of the target speed.
            else if ((m_targetSpeed > 0 && pos_r > pos_l) || (m_targetSpeed < 0 && pos_l > pos_r)) {
                SmartDashboard.putString("Lifter Debug", "Slow R");
                motorRight.set(ControlMode.PercentOutput, m_targetSpeed * SLOW_SPEED_MULTIPLIER);
                motorLeft.set(ControlMode.PercentOutput, m_targetSpeed);
            }

            else if ((m_targetSpeed > 0 && pos_r < pos_l) || (m_targetSpeed < 0 && pos_l < pos_r)) {
                SmartDashboard.putString("Lifter Debug", "Slow L");
                motorLeft.set(ControlMode.PercentOutput, m_targetSpeed * SLOW_SPEED_MULTIPLIER);
                motorRight.set(ControlMode.PercentOutput, m_targetSpeed);
            }

        }
    }

    public void set(int position) {
        motorSet(Lifter.LIFTING_POWER);
        this.StartClimb = true;
        m_pos = position;
    }

    public void Lift() {
        // Tell autolift to climb
        motorSet(Lifter.LIFTING_POWER);
        this.StartClimb = true;
        m_pos = Lifter.LIFTED_POS;
    }

    public void Tuck() {
        // start with percent output to keep the lifter tucked under the belly.
        motorSet(Lifter.TUCKED_POWER);
        this.StartClimb = true;
        m_pos = Lifter.STARTING_POS;
    }

    public void Stop() {
        motorSet(Lifter.STOP_POWER);
        this.StartClimb = false;
        m_pos = motorRight.getSelectedSensorPosition();
    }

    private void motorSet(double value) {
        m_targetSpeed = value;
        motorLeft.set(ControlMode.PercentOutput, value);
        motorRight.set(ControlMode.PercentOutput, value);
    }

    public boolean IsAtSetpoint() {
        return Math.abs(motorLeft.getPosition() - m_pos) < Lifter.IN_POSITION_SLOP;
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Lifter Pos R", motorRight.getSelectedSensorPosition());
        SmartDashboard.putNumber("Lifter Pos L", motorLeft.getSelectedSensorPosition());
    }

}
