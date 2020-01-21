package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class WaitForever extends CommandBase {

    public WaitForever() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return false;
    }
}
