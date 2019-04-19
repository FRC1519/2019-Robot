package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.Wrist;
import org.mayheminc.robot2019.subsystems.Targeting.TargetHeight;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmMove extends Command {
	double m_startingShoulderAngle;
	double m_startingWristInternalAngle;
	double m_targetShoulderAngle;
	double m_targetWristAngle;
	double m_targetWristInternalAngle;

	double m_deltaShoulderAngle;
	double m_deltaWristInternalAngle;

	Command m_subCommand = null;

	double m_timeout;
	Timer m_timer = new Timer();

	private static double DEFAULT_TIMEOUT = 3000.0; // default timeout of 3 seconds

	/**
	 * Move the arm to a specified position, with no time limit. The command returns
	 * only after the arm has moved to the desired position.
	 * 
	 * @param targetShoulderAngle Target angle for shoulder position in degrees,
	 *                            from "world" perspective
	 * @param targetWristAngle    Target angle for wrist position in degrees, from
	 *                            "world" perspective
	 */
	public ArmMove(double targetShoulderAngle, double targetWristAngle) {
		this(targetShoulderAngle, targetWristAngle, DEFAULT_TIMEOUT);
	}

	public ArmMove(double targetShoulderAngle, double targetWristAngle, TargetHeight target) {
		this(targetShoulderAngle, targetWristAngle, DEFAULT_TIMEOUT);
		Robot.targeting.setTargetHeight(target);
	}

	/**
	 * Move the arm to a specified position, with a time limit. The command returns
	 * upon the first of either the arm having moved to the desired position or the
	 * time limit having been reached.
	 * 
	 * @param targetShoulderAngle Target angle for shoulder position in degrees,
	 *                            from "world" perspective
	 * @param targetWristAngle    Target angle for wrist position in degrees, from
	 *                            "world" perspective
	 * @param timeLimit           Time limit for the command, in seconds
	 */
	public ArmMove(double targetShoulderAngle, double targetWristAngle, double timeLimit) {
		m_timeout = timeLimit;
		m_targetShoulderAngle = targetShoulderAngle;
		m_targetWristAngle = targetWristAngle;
		m_targetWristInternalAngle = Wrist.computeInternalAngle(m_targetShoulderAngle, m_targetWristAngle);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// if there is an old command from a prior invocation still hanging around,
		// let's make sure it has been cancelled
		if (m_subCommand != null) {
			m_subCommand.cancel();
			m_subCommand = null;
		}

		// record needed information about current arm "pose"
		m_startingShoulderAngle = Robot.shoulder.getAngleInDegrees();
		m_startingWristInternalAngle = Robot.wrist.getInternalAngleInDegrees();

		m_deltaShoulderAngle = Math.abs(m_startingShoulderAngle - m_targetShoulderAngle);
		m_deltaWristInternalAngle = Math.abs(m_startingWristInternalAngle - m_targetWristInternalAngle);

		// prepare the timeout
		m_timer.reset();
		m_timer.start();

		// Basic approach: at initialization, decide the strategy.
		// There are three basic strategies from which to choose:
		// 1 - Move the shoulder and wrist simultaneously
		// 2 - Move the shoulder first, then the wrist
		// 3 - Move the wrist first, then the shoulder

		// Other observations:
		// * if the shoulder needs to go up, always safe to move the shoulder first,
		// then the wrist second
		// * if the shoulder needs to go down, always safe to move the wrist first, then
		// the shoulder second

		if (m_targetShoulderAngle >= m_startingShoulderAngle) {
			// moving shoulder up

			// if the starting shoulder angle is above -16 degrees, okay to move wrist
			// simultaneously
			if (m_startingShoulderAngle >= -16.0) {
				m_subCommand = new ArmMoveSimultaneous(m_targetShoulderAngle, m_targetWristAngle);
			} else {
				// always safe to move shoulder up first and then move the wrist second
				// just need to give the shoulder a little bit (0.2 seconds) of a head start
				m_subCommand = new ArmMoveWithShoulderHeadStart(m_targetShoulderAngle, m_targetWristAngle, 0.2);
			}
		} else {
			// moving the shoulder down
			if (m_targetShoulderAngle >= -16.0) {
				// target height is above the point where "crashing" can happen,
				// always okay to move both simultaneously
				m_subCommand = new ArmMoveSimultaneous(m_targetShoulderAngle, m_targetWristAngle);
			} else if (m_deltaShoulderAngle >= m_deltaWristInternalAngle) {
				// the shoulder needs to move farther than the wrist, move them
				// simultaneously
				m_subCommand = new ArmMoveSimultaneous(m_targetShoulderAngle, m_targetWristAngle);
			} else {
				// getting to this section of code means that the wrist must need to move
				// farther than the shoulder.

				// always safe to move the wrist first and then the shoulder second, but we're
				// going to try just giving the wrist a "head start"

				// Compute how much further the wrist needs to rotate than the shoulder
				double wristExtraDistance = m_deltaWristInternalAngle - m_deltaShoulderAngle;

				// give the wrist a head start of 1 second per 120 degrees
				m_subCommand = new ArmMoveWithWristHeadStart(m_targetShoulderAngle, m_targetWristAngle,
						wristExtraDistance / 120.0);
			}
		}

		if (m_subCommand != null) {
			m_subCommand.start();
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// Nothing needs to happen in "execute" -- arm movement is actually controlled
		// by subsidiary commands
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// Return when all of the subsidiary commands that were created have completed

		// TODO: Timeout feature not actually implemented yet!!!
		return m_subCommand.isCompleted();
	}

	// Called once after isFinished returns true
	protected void end() {
		// Note - when the ArmMove command isFinished, the subsidiary command will have
		// completed, leaving the arm in a "position control" status to go to the
		// current position, whatever that was.
		m_subCommand = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		// cancel any ongoing subcommand
		m_subCommand.cancel();
		m_subCommand = null;

		// Have the arm and shoulder hold their current positions
		Robot.shoulder.setDesiredAngle(Robot.shoulder.getAngleInDegrees());
		Robot.wrist.setDesiredAngle(Robot.wrist.getAngleInDegrees());
	}
}
