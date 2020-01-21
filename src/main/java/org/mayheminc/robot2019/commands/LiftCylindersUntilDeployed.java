/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.LiftCylinders;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Remember the roll of the NavX
// Deplay Cylinders
// IsFinished when the roll is -15 from initial
public class LiftCylindersUntilDeployed extends CommandBase {

  double m_startingAngle;

  public LiftCylindersUntilDeployed() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.liftCylinders);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    m_startingAngle = Robot.drive.getPitch();
    Robot.liftCylinders.set(LiftCylinders.EXTENDED);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return ((Robot.drive.getPitch() - m_startingAngle) < -4.0); // was -7.0 at Pine Tree
  }

  // Called once after isFinished returns true or when interrupted
  @Override
  public void end(boolean interrupted) {
  }
}
