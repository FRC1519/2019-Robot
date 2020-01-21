package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 *
 */
public class DriveSetShifter extends InstantCommand {
    boolean position;

    public DriveSetShifter(boolean arg_position) {
        position = arg_position;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.shifter.setGear(position);
    }
}
