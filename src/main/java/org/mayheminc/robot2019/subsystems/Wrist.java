package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Wrist extends Subsystem {

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.WRIST_TALON);

    public final int CARGO_PICK_UP_POSITION = 1000;

    public void initDefaultCommand() {
    }

    public void set(int x) {
        motor.setEncPosition(x);

    }
}
