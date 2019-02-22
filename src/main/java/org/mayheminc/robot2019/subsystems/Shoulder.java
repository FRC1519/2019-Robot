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

import org.mayheminc.util.MayhemTalonSRX;

public class Shoulder extends Subsystem {

    public static int STARTING_POS = 0;
    private static int IN_POSITION_SLOP = 100;
    private static double BRAKE_RELASE_TIME_SEC = 0.1;

    private MayhemTalonSRX motor_A = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);
    private MayhemTalonSRX motor_B = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_B);
    private Solenoid brake = new Solenoid(RobotMap.SHOULDER_BRAKE_SOLENOID);

    private double m_currentPosition = 0.0;
    private double m_pos;

    enum State {
        STOPPED, START_MOVING, WAIT_FOR_BRAKE_RELEASE, MOTOR_MOVING,
    };

    State m_state = State.STOPPED;
    Timer m_timer = new Timer();
    double m_startTimer;

    boolean manualMode = true; // TODO: debug. Set to false for init

    public Shoulder() {

        // motor_B.changeControlMode(ControlMode.Follower);
        // motor_B.set(motor_A.getDeviceID());

        configMotor(motor_A);
        configMotorFollower(motor_B, motor_A.getDeviceID());
        // motor_A.config_kP(0, 1.0, 0);
        // motor_A.config_kI(0, 0.0, 0);
        // motor_A.config_kD(0, 0.0, 0);
        // motor_A.config_kF(0, 0.0, 0);

        // motor_A.setNeutralMode(NeutralMode.Coast);
        // motor_A.configNominalOutputVoltage(+0.0f, -0.0f);
        // motor_A.configPeakOutputVoltage(+12.0, -12.0);
        // motor_A.setFeedbackDevice(FeedbackDevice.QuadEncoder);

    }

    void configMotorFollower(MayhemTalonSRX motor, int id) {
        configMotor(motor);

        motor.changeControlMode(ControlMode.Follower);
        motor.set(id);
    }

    void configMotor(MayhemTalonSRX motor) {
        motor.config_kP(0, 1.0, 0);
        motor.config_kI(0, 0.0, 0);
        motor.config_kD(0, 0.0, 0);
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
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

    public int getCurrentPosition() {
        // TODO:  should really get the position once per periodic cycle and "cache" it in a m_currentPosition field
        return (int) motor_A.getPosition();
    }

    public void initDefaultCommand() {
    }

     // updateSensors() should be called on every main loop, whether robot is disabled, autonomous, or teleop
     public void updateSensors() {
        m_currentPosition = motor_A.getPosition();
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Shoulder Current Pos", getCurrentPosition());
        SmartDashboard.putNumber("Shoulder Voltage A", motor_A.getOutputVoltage());
        SmartDashboard.putNumber("Shoulder Voltage B", motor_B.getOutputVoltage());
        SmartDashboard.putString("Shoulder State", m_state.toString());
        SmartDashboard.putBoolean("Shoulder Brake", brake.get());
    }

    public void setManualMode(boolean b) {
        this.manualMode = b;
    }

    public void update() {

        if (!manualMode) { // auto mode...
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
                    this.motor_A.set(ControlMode.Position, m_pos);
                }
                break;

            case MOTOR_MOVING:
                // If we are close to position...
                if (Math.abs(getCurrentPosition() - m_pos) < IN_POSITION_SLOP) {
                    // turn off the motor.
                    this.motor_A.set(ControlMode.PercentOutput, 0);
                    // set the brake
                    this.brake.set(true);
                    m_state = State.STOPPED;
                }
                break;
            }
        }
        // else // manual mode...
        // {
        // m_state = State.STOPPED;
        // this.brake.set(false);

        // this.motor_A.set(ControlMode.PercentOutput, Robot.oi.getOperatorLeftY());

        // }

        if (this.manualMode == true && Robot.oi.getOperatorLeftY() == 0.0) {
            motor_A.set(ControlMode.Position, getCurrentPosition());
            this.manualMode = false;
            m_state = State.STOPPED;
            this.brake.set(true);
        }

        if (Robot.oi.getOperatorLeftY() != 0.0) {
            this.manualMode = true;
            this.brake.set(false);
            motor_A.set(ControlMode.PercentOutput, Robot.oi.getOperatorLeftY());
        }
    }
}