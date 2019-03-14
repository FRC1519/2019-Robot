/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.mayheminc.robot2019.Robot;

/**
 * Add your docs here.
 */
public class LiftCylindersSetOnlyWhileHeld extends Command {
  /**
   * Add your docs here.
   */
  boolean m_pos;

  public LiftCylindersSetOnlyWhileHeld(boolean b) {
    super();
    // Use requires() here to declare subsystem dependencies
    requires(Robot.liftCylinders);
    m_pos = b;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.liftCylinders.set(m_pos);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.liftCylinders.set(!m_pos);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.liftCylinders.set(!m_pos);
  }

}