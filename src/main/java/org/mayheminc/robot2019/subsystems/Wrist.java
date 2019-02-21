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
    public static int STARTING_POS = 0;
    private static int IN_POSITION_SLOP = 100;

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.WRIST_TALON);
    private int m_desiredPostion;
    private boolean m_manualMode = false;

    public final int CARGO_PICK_UP_POSITION = 1000;

    public Wrist() {
        motor.config_kP(0, 1.0, 0);
        motor.config_kI(0, 0.0, 0);
        motor.config_kD(0, 0.0, 0);
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+6.0, -6.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    public void zero() {
        // zero the position.
        motor.setSelectedSensorPosition(STARTING_POS);
    }

    public void set(int pos) {
        m_desiredPostion = pos;
        this.m_manualMode = false;
        motor.set(ControlMode.Position, pos);
    }

    public boolean IsAtSetpoint() {
        return Math.abs(get() - m_desiredPostion) < Wrist.IN_POSITION_SLOP;
    }

    public int get() {
        return (int) motor.getPosition();
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Wrist Desired", m_desiredPostion);
        SmartDashboard.putNumber("wrist postion", get());
    }

    public void update() {
        // If we are not moving the arm and we are in manual mode, keep the position.
        if (this.m_manualMode == true && Robot.oi.getOperatorRightY() == 0.0) {
            motor.set(ControlMode.Position, get());
            this.m_manualMode = false;
        }

        if (Robot.oi.getOperatorRightY() != 0.0) {
            this.m_manualMode = true;
            motor.set(ControlMode.PercentOutput, Robot.oi.getOperatorRightY());
        }
    }
}
