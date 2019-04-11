/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.CargoIntakeSetForTime;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoShipFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoShipFront(Autonomous.StartOn startSide) {

    // Zero the Gyro at the start of autonomous
    addParallel(new DriveSetShifter(Shifter.LOW_GEAR));
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2 and wait a second to stop bouncing around
    addSequential(new DriveStraightOnHeading(0.9, 96, Autonomous.chooseAngle(startSide, 0.0))); // Drive 60 inches at a
                                                                                                // heading of zero
                                                                                                // degrees
    addSequential(new Wait(1.0));

    // Head for the cargo ship
    addSequential(new DriveStraightOnHeading(0.7, 42, Autonomous.chooseAngle(startSide, 270.0))); // Drive three more
                                                                                                  // feet turning left.

    // Get the arm into postion while lining to put the hatch panel on the ship.
    addParallel(new HatchPanelLow());
    addSequential(new DriveStraightOnHeading(0.7, 48, Autonomous.chooseAngle(startSide, 0.0)));

    addSequential(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));

    addSequential(new AutoAlignUntilAtWall(0.5, 1, TargetPosition.CENTER_MOST));

    // stop now to let the driver's take over!
    addSequential(new DriveSetShifter(Shifter.HIGH_GEAR));

  }
}
