package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchPanelPickUp extends Subsystem {

    Solenoid solenoid = new Solenoid(RobotMap.HATCH_PANEL_SOLENOID);

    public void initDefaultCommand() {
    }
}