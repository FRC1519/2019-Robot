/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import org.mayheminc.robot2019.commands.CargoIntakeSetForTime;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.Shifter;

public class StraightOffHAB2 extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public StraightOffHAB2() {

    addCommands(
        // Ensure Low Gear and Zero the Gyro at the start of autonomous
        new ParallelCommandGroup(
            // set high gear and zero the gyro in parallel
            new DriveSetShifter(Shifter.LOW_GEAR), //
            new ZeroGyro(0.0) //
        ),

        // Drive off hab level 2 and wait a second to stop bouncing around
        new DriveStraightOnHeading(0.9, 96, 0), // Drive 60 inches at a heading of zero degrees
        new Wait(1.0),

        // Drive a foot further to ensure we're clear of HAB Level 1
        new DriveStraightOnHeading(0.7, 12, 0), // Drive one more foot at a heading of zero degrees

        // turn on the cargo intake to free the wrist from the velcro wrist retainer
        new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5),

        // stop now to let the drivers take over!
        new DriveSetShifter(Shifter.HIGH_GEAR));
  }
}
