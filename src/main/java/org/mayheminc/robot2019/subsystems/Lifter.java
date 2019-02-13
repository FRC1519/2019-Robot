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
    private static final double LIFTING_POWER = 0.3; // debug speed

    private static final double CHANGE_POWER = 0.1;

    // constants for positions
    private static final int STARTING_POS = 0;
    private static final int IN_POSITION_SLOP = 100;
    private static final int MAX_MOTOR_OFFSET = 5000;

    // private static final int LIFTED_POS = 1000000; // 1 million ticks
    private static final int LIFTED_POS = 100000; // 100k ticks debug tick count

    private final MayhemTalonSRX motorLeft = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRight = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    // private final MayhemTalonSRX motorLeftB = new
    // MayhemTalonSRX(RobotMap.LIFTER_LEFT_B_TALON);
    // private final MayhemTalonSRX motorRightB = new
    // MayhemTalonSRX(RobotMap.LIFTER_RIGHT_B_TALON);

    private int m_pos;

    private Boolean StartClimb = false;

    public Lifter() {

        // Config right motor
        ConfigMotor(motorLeft, false);
        ConfigMotor(motorRight, true);

        // ConfigMotorFollower(motorLeftB, motorLeft);
        // ConfigMotorFollower(motorRightB, motorRight);

        // Tuck();
    }

    /**
     * Set a motor to follow another motor
     */
    private void ConfigMotorFollower(MayhemTalonSRX follower, MayhemTalonSRX following) {
        follower.changeControlMode(ControlMode.Follower);
        follower.set(following.getDeviceID());
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
        // motorLeftB.setSelectedSensorPosition(0); // start at 0
        motorRight.setSelectedSensorPosition(0); // start at 0
        // motorRightB.setSelectedSensorPosition(0); // start at 0
        Stop();
    }

    public void AutoLift() {
        // Get the encoder postions
        if (this.StartClimb) {
            int pos_r = motorRight.getSelectedSensorPosition();
            int pos_l = motorLeft.getSelectedSensorPosition();

            // if the positions are close together, then lift together.
            if (Math.abs(pos_r - pos_l) < MAX_MOTOR_OFFSET) {
                motorRight.set(ControlMode.PercentOutput, LIFTING_POWER);
                motorLeft.set(ControlMode.PercentOutput, LIFTING_POWER);
            }

            // If one motor is far ahead of the other, slow down 1 side.
            else if (pos_r > pos_l) {
                motorRight.set(ControlMode.PercentOutput, LIFTING_POWER - CHANGE_POWER);
            } else if (pos_r < pos_l) {
                motorLeft.set(ControlMode.PercentOutput, LIFTING_POWER - CHANGE_POWER);
            }

            // Stop if done climbing
            if (pos_r >= LIFTED_POS) {
                motorRight.set(ControlMode.PercentOutput, STOP_POWER);
                motorLeft.set(ControlMode.PercentOutput, STOP_POWER);
                this.StartClimb = false;
            }
        }
    }

    public void Lift() {
        // Tell autolift to climb
        motorSet(LIFTING_POWER);

        this.StartClimb = true;

        // set the target position
        m_pos = Lifter.LIFTED_POS;
    }

    public void Tuck() {
        // start with percent output to keep the lifter tucked under the belly.
        motorSet(TUCKED_POWER);
        this.StartClimb = false;

        m_pos = Lifter.STARTING_POS;
    }

    public void Stop() {
        motorSet(STOP_POWER);
        this.StartClimb = false;
    }

    private void motorSet(double value) {

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
