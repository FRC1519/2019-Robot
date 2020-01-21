package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.mayheminc.robot2019.Robot;

/**
 *
 */
public class DriveRotateDegrees extends CommandBase {
    final static double DEFAULT_TIME_LIMIT_SEC = 2.0;
    final static double FINAL_HEADING_TOLERANCE = 10.0;
    double timeLimit = 0;
    double degrees = 0;
    Timer timer = new Timer();

    public DriveRotateDegrees(double arg) {
        this(arg, DEFAULT_TIME_LIMIT_SEC);
    }

    public DriveRotateDegrees(double arg, double limitForTime) {
        degrees = arg;
        timeLimit = limitForTime;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.drive.rotate(degrees);
        // TODO: RJD: does this need a timer.reset()?
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        Robot.drive.speedRacerDrive(0.05, 0.0, true);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        double actual = Robot.drive.getHeading();
        double desired = Robot.drive.getDesiredHeading();
        double diff = actual - desired;
        diff = Math.abs(diff);

        return ((diff < FINAL_HEADING_TOLERANCE) || (timer.get() > timeLimit));
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        Robot.drive.speedRacerDrive(0.0, 0.0, false);
    }
}
