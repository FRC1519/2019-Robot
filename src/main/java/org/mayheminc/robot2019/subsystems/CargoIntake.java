package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.RobotMap;
import org.mayheminc.util.MayhemTalonSRX;

public class CargoIntake extends Subsystem {

    public static final double INTAKE_HARD_POWER = 1.0;
    public static final double INTAKE_SOFT_PANEL = 0.6;
    public static final double HOLD_POWER = 0.05;
    public static final double OFF_POWER = 0.0;
    public static final double OUTTAKE_SOFT_POWER = -0.6;
    public static final double OUTTAKE_HARD_POWER = -1.0;

    private final MayhemTalonSRX m_motor = new MayhemTalonSRX(RobotMap.INTAKE_ROLLER_TALON);
    double m_power;

    public CargoIntake() {
        m_motor.setNeutralMode(NeutralMode.Coast);
        m_motor.configNominalOutputVoltage(+0.0f, -0.0f);
        m_motor.configPeakOutputVoltage(+12.0, -12.0);
        m_motor.setInverted(true);
        this.setPower(CargoIntake.OFF_POWER);
    }

    public void initDefaultCommand() {
    }

    public void setPower(double power) {
        m_power = power;
        m_motor.set(ControlMode.PercentOutput, m_power);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Cargo Intake Power", m_power);
        SmartDashboard.putNumber("Cargo Intake Amps", m_motor.getOutputCurrent());
    }

    public void update() {
    }
}
