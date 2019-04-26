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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WristReZeroLive extends Command {
  public int m_currentPosition;
  public int m_lastPosition;
  public int m_loopsThatWeHaveBeenStoped = 0;

  public WristReZeroLive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.wrist);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.wrist.setPercentOutput(0.5);
    m_lastPosition = Robot.wrist.getCurrentPosition();
    m_loopsThatWeHaveBeenStoped = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    m_currentPosition = Robot.wrist.getCurrentPosition();
    if (Math.abs(m_lastPosition - m_currentPosition) < 10) {
      m_loopsThatWeHaveBeenStoped++;
    } else {
      m_loopsThatWeHaveBeenStoped = 0;
    }
    m_lastPosition = m_currentPosition;
    SmartDashboard.putNumber("m_loopsThatWeHaveBeenStoped", m_loopsThatWeHaveBeenStoped);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // m_currentPosition = Robot.wrist.getCurrentPosition();
    // If there is a high current level, exit

    // TODO: Should really instead look for when the encoder hasn't turned for 10
    // cycles. Maybe Robot.wrist.isEncoderStalled() would be a good method to use?
    return (m_loopsThatWeHaveBeenStoped >= 10);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // Zero the arm

    // "zero" from a live wrist zero ends up being 10 degrees too high
    // (Note 10 degreess is about 100 ticks)
    Robot.wrist.zeroWithOffset(120);
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
