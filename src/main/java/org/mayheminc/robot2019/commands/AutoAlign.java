/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class AutoAlign extends Command {

  /*
   * AutoAlign the robot, presuming that the robot is being driven forwards and
   * backwards by the driver.
   */
  public AutoAlign() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.drive.setAutoAlignTrue();
    Robot.targetingLights.set(true);
    Robot.targeting.setMode(TargetPosition.CENTER_MOST);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drive.setAutoAlignFalse();
    Robot.targetingLights.set(false);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    Robot.drive.setAutoAlignFalse();
    Robot.targetingLights.set(false);
  }
}
