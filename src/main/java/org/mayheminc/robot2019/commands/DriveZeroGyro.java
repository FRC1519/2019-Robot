package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class DriveZeroGyro extends CommandBase {

    public DriveZeroGyro() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.drive.zeroHeadingGyro(0.0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
    }
}
