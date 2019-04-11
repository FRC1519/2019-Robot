/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.LiftCylinders;

import edu.wpi.first.wpilibj.command.Command;

// Remember the roll of the NavX
// Deplay Cylinders
// IsFinished when the roll is -15 from initial
public class LiftCylindersUntilDeployed extends Command {

  double m_startingAngle;

  public LiftCylindersUntilDeployed() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.liftCylinders);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_startingAngle = Robot.drive.getPitch();
    Robot.liftCylinders.set(LiftCylinders.EXTENDED);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return ((Robot.drive.getPitch() - m_startingAngle) < -4.0); // was -7.0 at Pine Tree
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
