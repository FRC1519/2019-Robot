package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;
import org.mayheminc.util.MayhemTalonSRX;

public class CargoIntake extends SubsystemBase {

    public static final double INTAKE_HARD_POWER = 1.0;
    public static final double INTAKE_SOFT_PANEL = 0.6;
    public static final double HOLD_POWER = 0.20; // was 0.05
    public static final double OFF_POWER = 0.0;
    public static final double OUTTAKE_SOFT_POWER = -0.4;
    public static final double OUTTAKE_HARD_POWER = -1.0; // used to release the "velcro retainer"

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

    int m_autoStopCount;

    public void update() {
        // look for high current on the rollers.
        // stop the rollers when we get a ball.

        // if we are taking in a ball...
        if (m_power >= CargoIntake.INTAKE_SOFT_PANEL) {
            // if the current is high...
            if (m_motor.getOutputCurrent() > 10.0) {
                // count to 10...
                m_autoStopCount++;
                if (m_autoStopCount > 10) {
                    // fall back to a hold power for the ball.
                    this.setPower(CargoIntake.HOLD_POWER);
                    m_autoStopCount = 0;

                    Robot.lights.set(LedPatternFactory.cargoBallGotIt);

                    // if we are picking up from the floor, we now want to lift up the wrist a bit
                    // to get the ball off the floor
                }
            } else {
                m_autoStopCount = 0;
            }
        } else {
            m_autoStopCount = 0;
        }
    }
}
