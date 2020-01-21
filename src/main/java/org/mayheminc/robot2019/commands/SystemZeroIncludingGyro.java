/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * Add your docs here.
 */
public class SystemZeroIncludingGyro extends InstantCommand {
  /**
   * Tell all the subsystems to Zero the encoders.
   */
  public SystemZeroIncludingGyro() {
    super();

    // Use requires() here to declare subsystem dependencies
    // requires(Robot.shoulder);
    // requires(Robot.lifter);
    // requires(Robot.wrist);
    // requires(Robot.drive);
  }
  
  @Override
  public boolean runsWhenDisabled() {
      return true;
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    Robot.drive.zeroHeadingGyro(0);
    Robot.lifter.zero();
    Robot.shoulder.zero();
    Robot.wrist.zero();
  }

}
