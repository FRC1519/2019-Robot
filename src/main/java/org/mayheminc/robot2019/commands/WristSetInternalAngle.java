/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class WristSetInternalAngle extends CommandBase {
  double m_internalAngle;

  double m_timeout;
  Timer timer = new Timer();

  private static double DEFAULT_TIMEOUT = 2000.0; // default timeout of 2 seconds

  public WristSetInternalAngle(double angleInDegrees) {
    this(angleInDegrees, DEFAULT_TIMEOUT);
  }

  public WristSetInternalAngle(double angleInDegrees, double timeLimit) {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.wrist);

    m_timeout = timeLimit;
    m_internalAngle = angleInDegrees;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.wrist.setInternalAngle(m_internalAngle);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Robot.wrist.isAtInternalSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    // if interrupted, set desired angle to current angle to hold position
    if (interrupted) {
      Robot.wrist.setInternalAngle(Robot.wrist.getInternalAngleInDegrees());
    }
  }
}
