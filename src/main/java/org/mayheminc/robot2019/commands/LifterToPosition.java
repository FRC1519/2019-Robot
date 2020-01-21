/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LifterToPosition extends CommandBase {
  int m_position;
  double m_power;

  public LifterToPosition(int position, double power) {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.lifter);
    m_position = position;
    m_power = power;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    Robot.lifter.setPositionWithPower(m_position, m_power);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return Robot.lifter.isAtSetpoint();
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      Robot.lifter.stop();
    } else {
      // TODO:  KBS noticed while doing beta conversion that this "stop" was commented out for the "end" case -- not sure why?
      // Robot.lifter.stop();
    }
  }
}
