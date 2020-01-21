package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchPanelPickUp extends SubsystemBase {

    public static final boolean GRABBER_CONTRACTED = true;
    public static final boolean GRABBER_EXPANDED = false;

    boolean m_pos;
    Solenoid m_solenoid = new Solenoid(RobotMap.HATCH_PANEL_SOLENOID);

    public void initDefaultCommand() {
    }

    public void set(boolean b) {
        m_pos = b;
        m_solenoid.set(m_pos);

        Robot.eventServer.output((m_pos) ? "HatchOpen.wav" : "HatchClose.wav");
        Robot.lights.set(LedPatternFactory.hatchPanel);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Hatch Panel Pos", m_pos);
    }

}