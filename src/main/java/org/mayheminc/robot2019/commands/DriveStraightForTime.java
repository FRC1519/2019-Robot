package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraightForTime extends Command {
	double m_desiredPercentVbus = 0.0;
	double m_startTime = 0.0;
	double m_desiredTime;

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Time is in seconds
	 */
	public DriveStraightForTime(double desiredPercentVbus, double timeInSeconds) {
		requires(Robot.drive);
		m_desiredTime = timeInSeconds;
		m_desiredPercentVbus = desiredPercentVbus;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		m_startTime = Timer.getFPGATimestamp();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.speedRacerDrive(m_desiredPercentVbus, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double elapsedTime = Timer.getFPGATimestamp() - m_startTime;
		return (elapsedTime >= m_desiredTime);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();
	}

	protected void interrupted() {
		Robot.drive.stop();
	}
}
