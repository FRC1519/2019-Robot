package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class Message extends CommandBase {
	String text;
    public Message(String arg_text) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	text = arg_text;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    	DriverStation.reportError(text, false);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }
}
