package org.mayheminc.robot2019.subsystems;

// import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchPanelPickUp extends Subsystem {

    public static final boolean RELEASE = false;
    public static final boolean GRAB = true;

    boolean m_pos;
    Solenoid solenoid = new Solenoid(RobotMap.HATCH_PANEL_SOLENOID);

    public void initDefaultCommand() {
    }

    public void set(boolean b) {
        m_pos = b;
        this.solenoid.set(m_pos);
    }

    

    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Hatch Panel Pos", m_pos);
    }

}