/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Set the Lifter to a position and hold it there with PID control
 */
public class LifterSetPosition extends CommandBase {
  int m_position;

  public LifterSetPosition(int position) {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.lifter);

    m_position = position;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.lifter.setPosition(m_position);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Robot.lifter.isAtSetpoint();
  }

  // Called once after isFinished returns true or the command is interrupted
  @Override
  public void end(boolean interrupted) {
  }
}
