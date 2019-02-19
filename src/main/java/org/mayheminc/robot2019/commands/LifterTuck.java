/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Add your docs here.
 */
public class LifterTuck extends Command {
  /**
   * Add your docs here.
   */
  public LifterTuck() {
    super();
    // Use requires() here to declare subsystem dependencies
    requires(Robot.lifter);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.lifter.Tuck();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.lifter.IsAtSetpoint();
    // return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.lifter.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.lifter.stop();
  }
}
