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
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.CargoIntake;

public class StraightOffHAB2 extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StraightOffHAB2() {

    // Zero the Gyro at the start of autonomous
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2 and wait a second to stop bouncing around
    addSequential(new DriveStraightOnHeading(0.9, 96, 0)); // Drive 60 inches at a heading of zero degrees
    addSequential(new Wait(1.0));

    // Drive a foot further to ensure we're clear of HAB Level 1
    addSequential(new DriveStraightOnHeading(0.7, 12, 0)); // Drive one more foot at a heading of zero degrees

    addSequential(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));
    // stop now to let the driver's take over!
  }
}
