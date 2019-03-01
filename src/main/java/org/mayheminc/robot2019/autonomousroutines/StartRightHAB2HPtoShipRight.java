/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;

public class StartRightHAB2HPtoShipRight extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoShipRight() {

    // Zero the Gyro at the start of autonomous
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2 and wait a second to stop bouncing around
    addSequential(new DriveStraightOnHeading(0.9, 96, 0)); // Drive 60 inches at a heading of zero degrees
    addSequential(new Wait(1.0));

    // Head for the cargo ship
    addSequential(new DriveStraightOnHeading(0.7, 48, +60)); // Drive three more feet turning right.

    // Get the arm into postion while heading downfield alongside the cargo ship
    addParallel(new HatchPanelLow());
    addSequential(new DriveStraightOnHeading(0.7, 48, 0)); // head straight downfield

    // Turn towards the side of the cargo ship
    addSequential(new DriveStraightOnHeading(0.7, 60, -90));

    // stop now to let the driver's take over!
  }
}
