/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.ZeroGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartCargoRightToLoadingStation extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartCargoRightToLoadingStation() {
    addSequential(new ZeroGyro(-90.0));

    addSequential(new DriveStraightOnHeading(-0.5, 50, -170)); // Drive 50 inches backwards to face teh loading station

    addSequential(new DriveStraightOnHeading(0.2, 10, 160)); // Drive towards the loading station
    addSequential(new DriveStraightOnHeading(0.8, 250, 160)); // Drive towards the loading station

    addSequential(new DriveStraightOnHeading(0.8, 200, 180)); // Drive towards the loading station

  }
}
