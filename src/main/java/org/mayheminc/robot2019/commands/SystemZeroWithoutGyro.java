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
 * Add your docs here.
 */
public class SystemZeroWithoutGyro extends InstantCommand {
  /**
   * Tell all the subsystems to Zero the encoders.
   */
  public SystemZeroWithoutGyro() {
    super();

    // Use requires() here to declare subsystem dependencies
    // requires(Robot.shoulder);
    // requires(Robot.lifter);
    // requires(Robot.wrist);
    // requires(Robot.drive);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.lifter.zero();
    Robot.shoulder.zero();
    Robot.wrist.zero();
  }

}
