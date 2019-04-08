package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.Targeting;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoAlignUntilAtWall extends Command {

	double m_targetPower;
	double m_startTime;
	double m_maxTime;
	Targeting.TargetPosition m_whichTarget = Targeting.TargetPosition.CENTER_MOST;

	/**
	 * Auto-align the robot at the specified power until at the wall, subject to a
	 * timeout.
	 * 
	 * @param targetPower +/- motor power [-1.0, +1.0]
	 * @param maxTime     Maximum time (in seconds) for alignment.
	 * @param target      Which target (Left, Center, or Right) to use to align
	 */
	public AutoAlignUntilAtWall(double targetPower, double maxTime, Targeting.TargetPosition whichTarget) {
		requires(Robot.drive);
		m_maxTime = maxTime;
		m_targetPower = targetPower;
		m_whichTarget = whichTarget;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		m_startTime = Timer.getFPGATimestamp();
		// Set auto align to true so we auto align when speedRacerDrive is called
		Robot.drive.setAutoAlignTrue();
		// Setting the target to align to
		Robot.targeting.setMode(m_whichTarget);
		Robot.targetingLights.set(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drive.speedRacerDrive(m_targetPower, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double elapsedTime = Timer.getFPGATimestamp() - m_startTime;
		elapsedTime = Math.abs(elapsedTime);
		return (elapsedTime >= m_maxTime) || Robot.targeting.atWall();
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
