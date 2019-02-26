/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class WristSetInternalAngle extends Command {
  double m_internalAngle;

  public WristSetInternalAngle(double angleInDegrees) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.wrist);
    m_internalAngle = angleInDegrees;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.wrist.setInternalAngle(m_internalAngle);
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
    Robot.wrist.setInternalAngle(Robot.wrist.getInternalAngleInDegrees());
  }
}
