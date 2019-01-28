package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class CargoIntake extends Subsystem {

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.INTAKE_ROLLER_TALON);

    public void initDefaultCommand() {
    }

}