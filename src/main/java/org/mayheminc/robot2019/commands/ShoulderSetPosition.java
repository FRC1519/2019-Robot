/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShoulderSetPosition extends Command {
  int m_pos;

  public ShoulderSetPosition(int pos) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.shoulder);
    m_pos = pos;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.shoulder.set(m_pos);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.shoulder.IsAtSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.shoulder.set(Robot.wrist.get());
  }
}
