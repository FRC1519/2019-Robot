/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.LedPatternFactory;

import edu.wpi.first.wpilibj.command.Command;

public class WristReZeroLive extends Command {
  public WristReZeroLive() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.wrist.setPercentOutput(0.5);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // If there is a high current level, exit

    // TODO: Should really instead look for when the encoder hasn't turned for 10
    // cycles. Maybe Robot.wrist.isEncoderStalled() would be a good method to use?
    return Robot.wrist.isHighCurrent();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // Zero the arm
    Robot.wrist.zero();
    // Stop moving the wrist
    Robot.wrist.setPercentOutput(0.0);
    Robot.lights.set(LedPatternFactory.wristReZeroLive);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    // Stop moving the wrist
    Robot.wrist.setPercentOutput(0.0);
  }
}
