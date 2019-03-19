package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.robot2019.RobotMap;

public class LEDLights extends Subsystem {

    public static final double INTAKE_HARD_POWER = 1.0;
    public static final double INTAKE_SOFT_PANEL = 0.6;
    public static final double HOLD_POWER = 0.05;
    public static final double OFF_POWER = 0.0;
    public static final double OUTTAKE_SOFT_POWER = -0.6;
    public static final double OUTTAKE_HARD_POWER = -1.0;

    public enum PatternID {
        RAINBOW_RAINBOW_PALETTE(-0.99, "Rainbow Rainbow Palette"), HOT_PINK(0.57, "Solid Hot Pink"),
        DARK_RED(0.59, "Solid Dark Red"), RED(0.61, "Solid Red"), RED_ORANGE(0.63, "Solid Red Orange"),
        ORANGE(0.65, "Solid Orange"), GOLD(0.67, "Solid Gold"), YELLOW(0.69, "Solid Yellow"),
        LAWN_GREEN(0.71, "Solid Lawn Green"), BLUE_GREEN(0.79, "Solid Blue Green"), AQUA(0.81, "Solid Aqua"),
        SKY_BLUE(0.83, "Solid Sky Blue"), DARK_BLUE(0.85, "Solid Dark Blue"), BLUE(0.87, "Solid Blue"),
        BLUE_VIOLET(0.89, "Solid Blue Violet"), BLACK(0.99, "Solid Black");

        private final double m_pwmVal;
        private final String m_patternName;

        PatternID(double pwmVal, String patternName) {
            m_pwmVal = pwmVal;
            m_patternName = patternName;
        }

        private double getVal() {
            return m_pwmVal;
        }

        private String getName() {
            return m_patternName;
        }
    };

    private final Spark m_blinkin = new Spark(RobotMap.BLINKIN_LEDS_PWM);
    private PatternID m_currentPatternID = PatternID.BLACK;

    public LEDLights() {
        // the constructor defines the "default" pattern ID

        m_currentPatternID = PatternID.LAWN_GREEN;
        m_currentPatternID = PatternID.HOT_PINK;

        set(m_currentPatternID);
    }

    public void set(PatternID newPattern) {
        m_currentPatternID = newPattern;
        m_blinkin.set(newPattern.getVal());
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Blinkin Pattern ID", m_currentPatternID.getVal());
        SmartDashboard.putString("Blinkin Pattern Name", m_currentPatternID.getName());
    }

    public void update() {
    }
}
