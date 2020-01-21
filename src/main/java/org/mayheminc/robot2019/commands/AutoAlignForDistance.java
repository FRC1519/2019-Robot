package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.Targeting;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.mayheminc.robot2019.subsystems.Drive;

/**
 *
 */
public class AutoAlignForDistance extends CommandBase {

	double m_targetPower;
	double m_startTime;
	double m_maxTime;
	double m_desiredPercentVbus = 0.0;
	double m_desiredDistance = 0.0;
	Targeting.TargetPosition m_whichTarget = Targeting.TargetPosition.CENTER_MOST;

	public static enum DistanceUnits {
		ENCODER_TICKS, INCHES
	}

	/**
	 * Auto-align the robot at the specified power, subject to a timeout.
	 * 
	 * @param targetPower +/- motor power [-1.0, +1.0]
	 * @param maxTime     Maximum time (in seconds) for alignment.
	 * @param target      Which target (Left, Center, or Right) to use to align
	 */

	public AutoAlignForDistance(double desiredPercentVbus, DistanceUnits units, double desiredDistance, double maxTime,
			Targeting.TargetPosition whichTarget) {
		addRequirements(Robot.drive);
		m_desiredPercentVbus = desiredPercentVbus;
		if (units == DistanceUnits.INCHES) {
			desiredDistance = desiredDistance / Drive.DISTANCE_PER_PULSE;
		}
		m_desiredDistance = Math.abs(desiredDistance);
		m_maxTime = maxTime;
		m_whichTarget = whichTarget;
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
		Robot.drive.saveInitialWheelDistance();
		m_startTime = Timer.getFPGATimestamp();
		// Set auto align to true so we auto align when speedRacerDrive is called
		Robot.drive.setAutoAlignTrue();
		// Setting the target to align to
		Robot.targeting.setMode(m_whichTarget);
		Robot.targetingLights.set(true);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		Robot.drive.speedRacerDrive(m_desiredPercentVbus, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		// Get distance driven
		double distance = Math.abs(Robot.drive.getWheelDistance());
		// Get elapsed time
		double elapsedTime = Math.abs(Timer.getFPGATimestamp() - m_startTime);
		return (distance >= m_desiredDistance) || (elapsedTime >= m_maxTime);
	}

	// Called once after isFinished returns true or when interrupted
	@Override
	public void end(boolean interrupted) {
		Robot.drive.stop();
		Robot.drive.setAutoAlignFalse();
		// Robot.targetingLights.set(false);
	}
}
