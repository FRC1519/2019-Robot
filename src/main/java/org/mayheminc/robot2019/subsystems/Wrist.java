package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Wrist extends Subsystem {
    private static final int IN_POSITION_SLOP = 100;
    public static final int ZERO_POS = 4000;          // raised the wrist all the way, until touching arm
    public static final int STARTING_POS = ZERO_POS;
    public static final int DEBUG_A_POS = STARTING_POS - 900;
    public static final int DEBUG_B_POS = STARTING_POS -1800;
    public static final int CARGO_PICK_UP_POSITION = 1000;

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.WRIST_TALON);
    private int m_desiredPostion;
    private boolean m_manualMode = false;
    private int m_currentPosition = 0;

    public Wrist() {
        motor.config_kP(0, 1.0, 0);
        motor.config_kI(0, 0.0, 0);
        motor.config_kD(0, 0.0, 0);
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+6.0, -6.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        motor.setInverted(true);
        motor.setSensorPhase(true);

		// TODO:  kbs is not sure we really want to zero the sensor each time the code starts
		// does this mean that we always need to have the wrist in the "zero position" whenever we deploy code?
        zero();
    }

    public void zero() {
        // zero the position.
        motor.setSelectedSensorPosition(STARTING_POS);
        setDesiredPosition(getCurrentPosition());
    }

    public void setDesiredPosition(int pos) {
        m_desiredPostion = pos;
        this.m_manualMode = false;
        // motor.set(ControlMode.Position, pos);
    }

    public boolean isAtSetpoint() {
        return Math.abs(getCurrentPosition() - m_desiredPostion) < Wrist.IN_POSITION_SLOP;
    }

    public int getCurrentPosition() {
        return m_currentPosition;
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Wrist Desired Pos", m_desiredPostion);
        SmartDashboard.putNumber("Wrist Current Pos", getCurrentPosition());
        SmartDashboard.putNumber("Wrist Voltage", motor.getOutputVoltage());
    }

    public void updateSensors() {
        m_currentPosition = (int) motor.getPosition();
    }

    public void update() {
        // If we are not moving the arm and we are in manual mode, hold the wrist steady
        if (this.m_manualMode == true && Robot.oi.getOperatorRightY() == 0.0) {
            // hold the wrist steady by setting the desired position 
            // to the current position.
            // Note that the below command also sets manualMode to false
            setDesiredPosition(getCurrentPosition());
        }

        if (Robot.oi.getOperatorRightY() != 0.0) {
            this.m_manualMode = true;
            // motor.set(ControlMode.PercentOutput, Robot.oi.getOperatorRightY());
        }
    }
}
