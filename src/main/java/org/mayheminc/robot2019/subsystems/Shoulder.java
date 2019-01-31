package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Shoulder extends Subsystem {

    public static int STARTING_POS = 0;
    private static int IN_POSITION_SLOP = 100;

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);
    private int m_pos;

    public Shoulder() {

        motor.config_kP(0, 1.0, 0);
        motor.config_kI(0, 0.0, 0);
        motor.config_kD(0, 0.0, 0);
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        // zero the position.
        motor.setSelectedSensorPosition(0);
        set(STARTING_POS);
    }

    public void set(int pos) {
        m_pos = pos;
        motor.set(ControlMode.Position, pos);
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
    }

}