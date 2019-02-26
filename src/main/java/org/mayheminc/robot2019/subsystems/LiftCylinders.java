package org.mayheminc.robot2019.subsystems;

// import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Solenoid;

public class LiftCylinders extends Subsystem {

    public static final boolean RETRACTED = false;
    public static final boolean EXTENDED = true;

    boolean m_pos;
    Solenoid solenoid = new Solenoid(RobotMap.LIFTER_SOLENOID);

    public void initDefaultCommand() {
    }

    public void set(boolean b) {
        m_pos = b;
        this.solenoid.set(m_pos);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Lifter Cylinder Pos", m_pos);
    }

}