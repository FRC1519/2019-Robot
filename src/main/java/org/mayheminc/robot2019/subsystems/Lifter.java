package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Lifter extends Subsystem {

    private final MayhemTalonSRX motorLeft = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRight = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    public void initDefaultCommand() {
    }
}