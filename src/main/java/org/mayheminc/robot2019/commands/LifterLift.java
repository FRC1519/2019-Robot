/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LifterLift extends CommandBase {
  private int m_desiredCounts = 0;

  public LifterLift(int desiredCounts) {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.lifter);
    m_desiredCounts = desiredCounts;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.lifter.Lift(m_desiredCounts);
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

  // Called once after isFinished returns true or command is interrupted
  @Override
  public void end(boolean interrupted) {
    Robot.lifter.stop();
  }
}
