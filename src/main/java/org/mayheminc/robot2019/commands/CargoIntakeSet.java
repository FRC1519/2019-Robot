/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Set the CargoIntake to a power.
 */
public class CargoIntakeSet extends InstantCommand {
  /**
   * Add your docs here.
   */
  double m_power;

  public CargoIntakeSet(double power) {
    super();

    // Use requires() here to declare subsystem dependencies
    requires(Robot.cargoIntake);
    m_power = power;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.cargoIntake.set(m_power);
  }
}
