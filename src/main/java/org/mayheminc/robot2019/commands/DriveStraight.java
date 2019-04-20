package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraight extends Command {

	private static double DEFAULT_TIME_LIMIT = 100.0; // time is in seconds

	double m_desiredPercentVbus = 0.0;
	double m_desiredDistance = 0.0;
	public int m_startPos = 0;
	double m_timeLimit = DEFAULT_TIME_LIMIT; // time is in seconds
	double m_startTime = 0.0;

	public static enum DistanceUnits {
		ENCODER_TICKS, INCHES
	}

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Distance in encoder counts
	 */
	public DriveStraight(double desiredPercentVbus, DistanceUnits units, double desiredDistance) {
		this(desiredPercentVbus, units, desiredDistance, DEFAULT_TIME_LIMIT);
	}

	public DriveStraight(double desiredPercentVbus, DistanceUnits units, double desiredDistance, double timeLimit) {
		if (units == DistanceUnits.INCHES) {
			desiredDistance = desiredDistance / Drive.DISTANCE_PER_PULSE;
		}
		m_timeLimit = timeLimit;
		m_desiredPercentVbus = desiredPercentVbus;
		m_desiredDistance = Math.abs(desiredDistance);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.drive.saveInitialWheelDistance();
		m_startTime = Timer.getFPGATimestamp();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.speedRacerDrive(m_desiredPercentVbus, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double distance = Math.abs(Robot.drive.getWheelDistance());
		double elapsedTime = Timer.getFPGATimestamp() - m_startTime;

		return (distance >= m_desiredDistance) || (elapsedTime > m_timeLimit);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.stop();

	}
}
