package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.mayheminc.robot2019.OI;

import org.mayheminc.util.MayhemTalonSRX;

public class Shoulder extends Subsystem {

    public static int STARTING_POS = 0;
    private static int IN_POSITION_SLOP = 100;
    private static double BRAKE_RELASE_TIME_SEC = 0.1;

    private MayhemTalonSRX motor_A = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);
    private MayhemTalonSRX motor_B = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_B);
    private Solenoid brake = new Solenoid(RobotMap.SHOULDER_BRAKE_SOLENOID);

    private int m_pos;

    enum State {
        STOPPED, START_MOVING, WAIT_FOR_BRAKE_RELEASE, MOTOR_MOVING,
    };

    State m_state;
    Timer m_timer = new Timer();
    double m_startTimer;

    public Shoulder() {

        motor_B.changeControlMode(ControlMode.Follower);
        motor_B.set(motor_A.getDeviceID());

        motor_A.config_kP(0, 1.0, 0);
        motor_A.config_kI(0, 0.0, 0);
        motor_A.config_kD(0, 0.0, 0);
        motor_A.config_kF(0, 0.0, 0);

        motor_A.setNeutralMode(NeutralMode.Coast);
        motor_A.configNominalOutputVoltage(+0.0f, -0.0f);
        motor_A.configPeakOutputVoltage(+12.0, -12.0);
        motor_A.setFeedbackDevice(FeedbackDevice.QuadEncoder);

    }

    /**
     * Call to zero the arm. Set the brake. Set the motor to the starting position.
     */
    public void Zero() {
        brake.set(true); // set the brake.

        // zero the position.
        motor_A.setSelectedSensorPosition(STARTING_POS);

        set(STARTING_POS);
    }

    public void set(int pos) {
        m_pos = pos;
        m_state = State.START_MOVING;
    }

    public boolean IsAtSetpoint() {
        return Math.abs(motor_A.getPosition() - m_pos) < Shoulder.IN_POSITION_SLOP;
    }

    public int get() {
        return (int) motor_A.getPosition();
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Shoulder Pos", m_pos);
        // SmartDashboard.putString("Shoulder State", m_state.toString());
        SmartDashboard.putBoolean("Shoulder Brake", brake.get());
    }

    public boolean percentState = false;

    public void periodic() {

        switch (m_state) {

        case STOPPED:
            // when we are stopped, set the motor 0 power.
            this.motor_A.set(ControlMode.PercentOutput, 0);
            // set the brake.
            this.brake.set(true);
            break;

        case START_MOVING:
            // release the brake
            this.brake.set(false);
            // start a timer
            m_startTimer = m_timer.get();
            m_state = State.WAIT_FOR_BRAKE_RELEASE;
            break;

        case WAIT_FOR_BRAKE_RELEASE:
            // when the timer expires, set the motor to the desired position.
            if (m_timer.get() - m_startTimer >= BRAKE_RELASE_TIME_SEC) {
                motor_A.set(ControlMode.Position, m_pos);
            }
            break;

        case MOTOR_MOVING:
            // If we are close to position...
            if (Math.abs(get() - m_pos) < IN_POSITION_SLOP) {
                // turn off the motor.
                this.motor_A.set(ControlMode.PercentOutput, 0);
                // set the brake
                this.brake.set(true);
                m_state = State.STOPPED;
            }
            break;
        }

        if (Robot.oi.pivotArmPower() != 0.0) {
            this.brake.set(false);
            this.motor_A.set(ControlMode.PercentOutput, Robot.oi.pivotArmPower());
            percentState = true;
        } else if (percentState) {
            m_state = State.STOPPED;
            percentState = false;
        }

    }
}