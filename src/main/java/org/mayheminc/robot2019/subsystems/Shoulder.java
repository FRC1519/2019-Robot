package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Shoulder extends Subsystem {

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);

    public void initDefaultCommand() {
    }
}