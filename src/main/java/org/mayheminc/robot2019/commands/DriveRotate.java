package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.mayheminc.robot2019.Robot;

/**
 *
 */
public class DriveRotate extends CommandBase {

	final static double DEFAULT_TIME_LIMIT_SEC = 2.5;
	final static double FINAL_HEADING_TOLERANCE = 8.0;
	double timeLimit = 0;
	double degrees = 0;
	Timer timer = new Timer();

	public enum DesiredHeadingForm {
		ABSOLUTE, RELATIVE
	};

	private boolean isAbsolute;

	public DriveRotate(double arg, DesiredHeadingForm form) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		this(arg, form, DEFAULT_TIME_LIMIT_SEC);
	}

	public DriveRotate(double arg, DesiredHeadingForm form, double limitForTime) {
		// requires(Robot.drive);

		if (form == DesiredHeadingForm.ABSOLUTE) {
			isAbsolute = true;
		} else {
			isAbsolute = false;
		}
		degrees = arg;
		timeLimit = limitForTime;
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
		if (isAbsolute) {
			// Robot.drive.rotateToHeading(degrees);
			Robot.drive.setDesiredHeading(degrees);
		} else {
			// Robot.drive.rotate(degrees);
			degrees += Robot.drive.getHeading();
			Robot.drive.setDesiredHeading(degrees);
		}

		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		// throttle must be nonzero to engage the maintainHeading() code
		Robot.drive.speedRacerDrive(-0.05, 0, false);
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
		if (interrupted) {
			DriverStation.reportError("Interrupted RotateDegrees Command", false);
		} else {
			DriverStation.reportError("Completed RotateDegrees Command", false);
		}
		// tell the robot to stop turning
		Robot.drive.speedRacerDrive(0, 0, false);
	}
}
