/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Set the CargoIntake to a power.
 */
public class LifterSetManual extends CommandBase {
  /**
   * Add your docs here.
   */
  double m_power;

  public LifterSetManual(double power) {
    // super();

    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.lifter);
    m_power = power;
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    Robot.lifter.setManual(m_power);
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true or the command is interrupted
  @Override
  public void end(boolean interrupted) {
    // don't need to do anything here
  }
}
