/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShoulderSetAngle extends CommandBase {
  double m_desiredAngle;

  double m_timeout;
  Timer timer = new Timer();

  private static double DEFAULT_TIMEOUT = 2000.0; // default timeout of 2 seconds

  public ShoulderSetAngle(double angleInDegrees) {
    this(angleInDegrees, DEFAULT_TIMEOUT);
  }

  public ShoulderSetAngle(double angleInDegrees, double timeLimit) {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.shoulder);

    m_timeout = timeLimit;
    m_desiredAngle = angleInDegrees;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.shoulder.setDesiredAngle(m_desiredAngle);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Robot.shoulder.isAtSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    // if interrupted, set the desired angle to the current angle to hold position
    if (interrupted) {
      Robot.shoulder.setDesiredAngle(Robot.shoulder.getAngleInDegrees());
    }
  }
}
