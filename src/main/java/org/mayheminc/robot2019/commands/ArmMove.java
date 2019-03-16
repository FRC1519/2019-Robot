package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

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

	Command m_subCommand = null;

	double m_timeout;
	Timer timer = new Timer();

	private static double DEFAULT_TIMEOUT = 3000.0; // default timeout of 3 seconds

	private enum ArmMoveStrategy {
		BOTH_SIMULTANEOUS, SHOULDER_FIRST, WRIST_FIRST
	};

	private ArmMoveStrategy m_strategy = ArmMoveStrategy.BOTH_SIMULTANEOUS; // presume simultaneous motion as default

	public ArmMove(double arg_targetShoulderAngle, double arg_targetWristAngle) {
		this(arg_targetShoulderAngle, arg_targetWristAngle, DEFAULT_TIMEOUT);
	}

	public ArmMove(double arg_targetShoulderAngle, double arg_targetWristAngle, double timeLimit) {
		m_timeout = timeLimit;
		m_targetShoulderAngle = arg_targetShoulderAngle;
		m_targetWristAngle = arg_targetWristAngle;
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

		// prepare the timeout
		timer.reset();
		timer.start();

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
			// moving shoulder up, always safe to move shoulder up first and then move the
			// wrist second
			m_subCommand = new ArmMoveWithShoulderFirst(m_targetShoulderAngle, m_targetWristAngle);
		} else {
			// must be moving the shoulder down, always safe to move the wrist first and
			// then the shoulder second
			m_subCommand = new ArmMoveWithWristFirst(m_targetShoulderAngle, m_targetWristAngle);
		}

		if (m_subCommand != null) {
			m_subCommand.start();
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// TODO: Figure out what needs to go here. Ken's current thought is nothing
		// happens here.
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// TODO: we should return when all of the subsidiary commands that were created
		// have completed
		return m_subCommand.isCompleted();
	}

	// Called once after isFinished returns true
	protected void end() {
		// TODO: when we are done, we should have the arm hold the target position with
		// appropriate commands, if not already done
		m_subCommand = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		// TODO: want to have the arm and shoulder hold their current positions
		// Robot.drive.stop();
		m_subCommand.cancel();
		m_subCommand = null;
	}
}
