/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.subsystems.Autonomous;

public class StartRightHAB2HPtoRocketLowTwice extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoRocketLowTwice() {

    // Call the shared HAB2HPtoRocketFront routine, specifying our starting side
    addSequential(new HAB2HPtoRocketFront(Autonomous.StartOn.RIGHT, Autonomous.RocketHeight.LOW));

    // now run the routine to get a hatch panel from the loading station
    addSequential(new RocketFrontToLoadingStationFast(Autonomous.StartOn.RIGHT));

    // now run the routine to get a hatch panel from the loading station
    addSequential(new LoadingStationToRocketBack(Autonomous.StartOn.RIGHT, Autonomous.RocketHeight.LOW));
  }
}