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

    private MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);
    private Solenoid brake = new Solenoid(RobotMap.SHOULDER_BREAK_SOLENOID);

    private int m_pos;

    enum State {
        STOPPED, START_MOVING, WAIT_FOR_BRAKE_RELEASE, MOTOR_MOVING,
    };

    State m_state;
    Timer m_timer = new Timer();
    double m_startTimer;

    public Shoulder() {

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
        motor.setSelectedSensorPosition(STARTING_POS);

        set(STARTING_POS);
    }

    public void set(int pos) {
        m_pos = pos;
        m_state = State.START_MOVING;
    }

    public boolean IsAtSetpoint() {
        return Math.abs(motor.getPosition() - m_pos) < Shoulder.IN_POSITION_SLOP;
    }

    public int get() {
        return (int) motor.getPosition();
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Shoulder Pos", m_pos);
        SmartDashboard.putString("Shoulder State", m_state.toString());
        SmartDashboard.putBoolean("Shoulder Brake", brake.get());
    }

    public void periodic() {
        switch (m_state) {

        case STOPPED:
            // when we are stopped, set the motor 0 power.
            this.motor.set(ControlMode.PercentOutput, 0);
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
                motor.set(ControlMode.Position, m_pos);
            }
            break;

        case MOTOR_MOVING:
            // If we are close to position...
            if (Math.abs(get() - m_pos) < IN_POSITION_SLOP) {
                // turn off the motor.
                this.motor.set(ControlMode.PercentOutput, 0);
                // set the brake
                this.brake.set(true);
                m_state = State.STOPPED;
            }
            break;

        }
    }
}