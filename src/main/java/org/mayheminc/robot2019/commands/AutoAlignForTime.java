package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAlignForTime extends Command {

	double m_targetPower;
	double m_startTime;
	double m_desiredTime;

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Time is in seconds
	 */
	public AutoAlignForTime(double arg_targetSpeed, double timeInSeconds) {
		requires(Robot.drive);
		m_desiredTime = timeInSeconds;
		m_targetPower = arg_targetSpeed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		m_startTime = Timer.getFPGATimestamp();
		// Set auto align to true so we auto align when speedRacerDrive is called
		Robot.drive.setAutoAlignTrue();
		Robot.targetingLights.set(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.speedRacerDrive(m_targetPower, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double diff = Timer.getFPGATimestamp() - m_startTime;
		diff = Math.abs(diff);
		return (diff >= m_desiredTime);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();
		Robot.drive.setAutoAlignFalse();
		Robot.targetingLights.set(false);
	}

	protected void interrupted() {
		Robot.drive.stop();
		Robot.drive.setAutoAlignFalse();
		Robot.targetingLights.set(false);
	}
}
