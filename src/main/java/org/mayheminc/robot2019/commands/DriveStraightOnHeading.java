package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class DriveStraightOnHeading extends CommandBase {

	double m_targetPower;
	double m_desiredDisplacement;
	double m_desiredHeading;

	public enum DistanceUnits {
		ENCODER_TICKS, INCHES
	};

	public DriveStraightOnHeading(double arg_targetSpeed, double arg_distance, double heading) {
		this(arg_targetSpeed, DistanceUnits.INCHES, arg_distance, heading);
	}

	/**
	 * 
	 * @param arg_targetPower +/- motor power [-1.0, +1.0]
	 * @param arg_distance    Distance in encoder counts
	 */
	public DriveStraightOnHeading(double arg_targetSpeed, DistanceUnits units, double arg_distance, double heading) {

		if (units == DistanceUnits.INCHES) {
			arg_distance = arg_distance / Drive.DISTANCE_PER_PULSE;
		}
		m_targetPower = arg_targetSpeed;
		m_desiredDisplacement = Math.abs(arg_distance);
		m_desiredHeading = heading;
	}

	// Called just before this Command runs the first time
	@Override
	public void initialize() {
		Robot.drive.saveInitialWheelDistance();
		Robot.drive.setDesiredHeading(m_desiredHeading);
		// System.out.println("Starting Routine: Drive Straight On Heading");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	public void execute() {
		Robot.drive.speedRacerDrive(m_targetPower, 0, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		int displacement = (int) Robot.drive.getWheelDistance();

		displacement = Math.abs(displacement);
		// System.out.println("displacement" + displacement);

		return (displacement >= m_desiredDisplacement);
	}

	// Called once after isFinished returns true or the command is interrupted
	@Override
	public void end(boolean interrupted) {
		Robot.drive.stop();
	}
}
