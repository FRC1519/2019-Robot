/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class AutoAlign extends CommandBase {

  /*
   * AutoAlign the robot, presuming that the robot is being driven forwards and
   * backwards by the driver.
   */
  TargetPosition m_whichTarget;

  public AutoAlign(TargetPosition whichTarget) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    m_whichTarget = whichTarget;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.drive.setAutoAlignTrue();
    Robot.targetingLights.set(true);
    Robot.targeting.setMode(m_whichTarget);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true or if the command is interrupted
  @Override
  public void end(boolean interrupted) {
    // behavior is the same whether isFinished or interrupted
    Robot.drive.setAutoAlignFalse();
    // Robot.targetingLights.set(false);
  }
}
