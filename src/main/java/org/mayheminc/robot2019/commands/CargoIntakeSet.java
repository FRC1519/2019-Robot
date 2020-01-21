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
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Set the CargoIntake to a power.
 */
public class CargoIntakeSet extends CommandBase {
  /**
   * Add your docs here.
   */
  double m_power;

  public CargoIntakeSet(double power) {
    super();

    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.cargoIntake);
    m_power = power;
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    Robot.cargoIntake.setPower(m_power);
    DriverStation.reportError("CargoIntake Run", false);

    Robot.lights.set(LedPatternFactory.cargoBallTrying);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true or the command is interrupted
  @Override
  public void end(boolean interrupted) {
    Robot.cargoIntake.setPower(CargoIntake.HOLD_POWER);
  }
}
