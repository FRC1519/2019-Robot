/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LifterLift extends Command {
  static int m_countEnd = 0;
  static int m_countInterrupted = 0;
  private int m_desiredCounts = 0;

  public LifterLift(int desiredCounts) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.lifter);
    m_desiredCounts = desiredCounts;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.lifter.Lift(m_desiredCounts);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.lifter.IsAtSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.lifter.stop();
    SmartDashboard.putNumber("LifterLift End", m_countEnd);
    m_countEnd++;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run, or the button is released, or ...
  @Override
  protected void interrupted() {
    Robot.lifter.stop();
    SmartDashboard.putNumber("LifterLift Interrupt", m_countInterrupted);
    m_countInterrupted++;
  }
}
