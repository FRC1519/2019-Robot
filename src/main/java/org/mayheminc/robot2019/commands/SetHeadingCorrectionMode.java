package org.mayheminc.robot2019.commands;
import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class SetHeadingCorrectionMode extends CommandBase {
	private boolean headingCorrectionMode;

    public SetHeadingCorrectionMode(boolean arg_headingCorrectionMode) {
//    	requires(Robot.drive);
    	headingCorrectionMode = arg_headingCorrectionMode;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    	Robot.drive.setHeadingCorrectionMode(headingCorrectionMode);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }
}
