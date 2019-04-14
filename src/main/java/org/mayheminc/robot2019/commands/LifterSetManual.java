/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.LedPatternFactory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Set the CargoIntake to a power.
 */
public class LifterSetManual extends Command {
  /**
   * Add your docs here.
   */
  double m_power;

  public LifterSetManual(double power) {
    super();

    // Use requires() here to declare subsystem dependencies
    requires(Robot.lifter);
    m_power = power;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.lifter.setManual(m_power);
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // don't need to do anything here
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    // don't need to do anything here }
  }
}
