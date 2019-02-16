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
    private int m_pos;

    public final int CARGO_PICK_UP_POSITION = 1000;

    public Wrist() {
        motor.config_kP(0, 1.0, 0);
        motor.config_kI(0, 0.0, 0);
        motor.config_kD(0, 0.0, 0);
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    public void Zero() {
        // zero the position.
        motor.setSelectedSensorPosition(STARTING_POS);
    }

    public void set(int pos) {
        m_pos = pos;
        motor.set(ControlMode.Position, pos);
    }

    public boolean IsAtSetpoint() {
        return Math.abs(motor.getPosition() - m_pos) < Wrist.IN_POSITION_SLOP;
    }

    public int get() {
        return (int) motor.getPosition();
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Wrist Pos", m_pos);
    }
}
