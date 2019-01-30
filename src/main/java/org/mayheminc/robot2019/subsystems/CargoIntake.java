package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.RobotMap;
import org.mayheminc.util.MayhemTalonSRX;

public class CargoIntake extends Subsystem {

    public static final double INTAKE = 1.0;
    public static final double HOLD = 0.1;
    public static final double OFF = 0.0;
    public static final double OUTTAKE = -0.5;
    public static final double OUTTAKE_HARD = -1.0;

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.INTAKE_ROLLER_TALON);
    double m_power;

    public CargoIntake() {
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);

        this.set(CargoIntake.OFF);
    }

    public void initDefaultCommand() {
    }

    public void set(double power) {
        m_power = power;
        motor.set(ControlMode.PercentOutput, m_power);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Cargo Intake Power", m_power);
    }
}
