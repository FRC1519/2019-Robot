/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.subsystems.Autonomous;

public class StartLeftHAB2HPtoShipSide extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartLeftHAB2HPtoShipSide() {

    // Call the shared HAB2HPtoShipSide routine, specifying we are on the left side
    addSequential(new HAB2HPtoShipSide(Autonomous.StartOn.LEFT));

  }
}