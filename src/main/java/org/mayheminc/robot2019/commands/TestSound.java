/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * Add your docs here.
 */
public class TestSound extends InstantCommand {
  /**
   * Add your docs here.
   */
  public TestSound() {
    super();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  
  @Override
  public boolean runsWhenDisabled() {
      return true;
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    Robot.eventServer.output("Test.wav");
    DriverStation.reportError("Playing Test.wav", false);
  }

}
