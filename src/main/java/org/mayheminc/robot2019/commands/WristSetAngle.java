/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class WristSetAngle extends Command {
  double m_desiredAngle;

  double m_timeout;
  Timer timer = new Timer();

  private static double DEFAULT_TIMEOUT = 2000.0; // default timeout of 2 seconds

  public WristSetAngle(double angleInDegrees) {
    this(angleInDegrees, DEFAULT_TIMEOUT);
  }

  public WristSetAngle(double angleInDegrees, double timeLimit) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.wrist);

    m_timeout = timeLimit;
    m_desiredAngle = angleInDegrees;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.wrist.setDesiredAngle(m_desiredAngle);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.wrist.isAtSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.wrist.setDesiredAngle(Robot.wrist.getAngleInDegrees());
  }
}
