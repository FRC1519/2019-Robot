/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.CargoIntakeSetForTime;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.CargoIntake;

public class StartRightHAB2HPtoShipRight extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoShipRight() {

    // Zero the Gyro at the start of autonomous
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2 and wait a second to stop bouncing around
    addSequential(new DriveStraightOnHeading(0.9, 72, 0)); // was 96.0 Drive 60 inches at a heading of zero degrees
    // addSequential(new Wait(1.0));

    // Head for the cargo ship
    addSequential(new DriveStraightOnHeading(0.9, 96, +40)); // Drive three more feet turning right.

    // Get the arm into postion while heading downfield alongside the cargo ship
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));
    addSequential(new DriveStraightOnHeading(0.9, 116, 0)); // head straight downfield

    // Turn towards the side of the cargo ship; -90 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to end up on track.
    addSequential(new DriveStraightOnHeading(0.6, 66, -110));

    // stop now to let the drivers take over!

    // (first thing they should do is release the hatch panel)
  }
}
