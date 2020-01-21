package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */

// drives forward until the gyro detects a tilt

public class DriveUntilTilt extends CommandBase {

    public static final double MAX_TILT = 2.0;

    public DriveUntilTilt() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        // requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        // Robot.drive.positiveDriveStraight(0.25);

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        Robot.drive.speedRacerDrive(0.25, 0, false);

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return (Robot.drive.getPitch() >= MAX_TILT);
    }

    // Called once after isFinished returns true or the command is interrupted
    @Override
    public void end(boolean interrupted) {
        Robot.drive.stop();
    }
}
