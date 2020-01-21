/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.subsystems.CargoIntake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Set the CargoIntake to a power.
 */
public class CargoIntakeSetForTime extends CommandBase {
  /**
   * Add your docs here.
   */
  double m_power;
  double m_startTime;
  double m_desiredTime;

  public CargoIntakeSetForTime(double power, double timeInSeconds) {
    super();

    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.cargoIntake);
    m_power = power;
    m_desiredTime = timeInSeconds;
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    Robot.cargoIntake.setPower(m_power);
    m_startTime = Timer.getFPGATimestamp();
  }

  @Override
  public boolean isFinished() {
    double diff = Timer.getFPGATimestamp() - m_startTime;
    diff = Math.abs(diff);
    return (diff >= m_desiredTime);
  }

  // Called once after isFinished returns true or the command is interrupted
  @Override
  public void end(boolean interrupted) {
    Robot.cargoIntake.setPower(CargoIntake.HOLD_POWER);
  }
}
