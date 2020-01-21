/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.LedPatternFactory;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WristReZeroLive extends CommandBase {
  public int m_currentPosition;
  public int m_lastPosition;
  public int m_loopsThatWeHaveBeenStopped = 0;

  public WristReZeroLive() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.wrist);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.wrist.setPercentOutput(0.5);
    m_lastPosition = Robot.wrist.getCurrentPosition();
    m_loopsThatWeHaveBeenStopped = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {

    m_currentPosition = Robot.wrist.getCurrentPosition();
    if (Math.abs(m_lastPosition - m_currentPosition) < 10) {
      m_loopsThatWeHaveBeenStopped++;
    } else {
      m_loopsThatWeHaveBeenStopped = 0;
    }
    m_lastPosition = m_currentPosition;
    SmartDashboard.putNumber("m_loopsThatWeHaveBeenStopped", m_loopsThatWeHaveBeenStopped);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    // Finished when the encoder hasn't turned for 10 cycles.
    return (m_loopsThatWeHaveBeenStopped >= 10);
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {

    // regardless of whether we are interrupted or not, stop moving the wrist!
    Robot.wrist.setPercentOutput(0.0);

    if (interrupted) {
      // we may not be in the zero position; can't zero, so just stop the wrist motor
    } else {
      // should be in zero position, zero the wrist

      // "zero" from a live wrist zero ends up being ~10 degrees too high
      // (Note 10 degreess is about 100 ticks)
      Robot.wrist.zeroWithOffset(80); // was 120 at start of Friday at CMP; may have overcorrected with this

      // use the robot lights to indicate that re-zero succeeded
      Robot.lights.set(LedPatternFactory.wristReZeroLive);
    }
  }
}