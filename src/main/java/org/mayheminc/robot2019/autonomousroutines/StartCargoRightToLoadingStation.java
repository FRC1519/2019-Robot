/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartCargoRightToLoadingStation extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartCargoRightToLoadingStation() {
    // addSequential(new ZeroGyro(-90.0));

    addSequential(new DriveStraightOnHeading(-0.8, 50, -170)); // Drive 50 inches backwards to face the loading station

    addSequential(new DriveStraightOnHeading(0.6, 20, 160)); // Drive towards the loading station
    addSequential(new DriveStraightOnHeading(0.9, 210, 160)); // Drive towards the loading station

    addSequential(new DriveStraightOnHeading(0.9, 180, 180)); // Drive towards the loading station

  }
}
