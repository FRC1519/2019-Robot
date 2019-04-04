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
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoShipSide extends CommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoShipSide(Autonomous.StartOn startSide) {

    // Zero the Gyro at the start of autonomous
    addSequential(new ZeroGyro(0.0));

    // Drive straight forward off hab level 2
    addSequential(new DriveStraightOnHeading(0.9, 72, Autonomous.chooseAngle(startSide, 0.0)));

    // Head for the cargo ship, but mostly by aiming at the rocket for now
    // NOTE: Heading was 40.0 as of 2 April 2019

    addSequential(new DriveStraightOnHeading(0.7, 84, Autonomous.chooseAngle(startSide, 60.0)));

    // Get the arm into postion while heading downfield alongside the cargo ship
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));
    addSequential(new DriveStraightOnHeading(0.7, 150, Autonomous.chooseAngle(startSide, 0.0)));

    // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to get there quickly.
    addSequential(new DriveStraightOnHeading(0.6, 48, Autonomous.chooseAngle(startSide, 250.0)));

    // Use "AutoAlign" at half speed for the last second to drive to the hatch
    // addSequential(new AutoAlignForTime(0.6, 1.0));
    addSequential(new AutoAlignUntilAtWall(0.6, 1.5, TargetPosition.CENTER_MOST));

    // release the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
    addSequential(new Wait(0.3));

    // now run the routine to get a hatch panel from the loading station
    addSequential(new ShipSideToLoadingStation(startSide));

  }
}
