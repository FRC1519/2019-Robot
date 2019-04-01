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
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;

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
    addSequential(new DriveStraightOnHeading(0.9, 96, Autonomous.chooseAngle(startSide, 40.0)));

    // Get the arm into postion while heading downfield alongside the cargo ship
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));
    addSequential(new DriveStraightOnHeading(0.9, 116, Autonomous.chooseAngle(startSide, 0.0)));

    // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to get there quickly.
    addSequential(new DriveStraightOnHeading(0.6, 66, Autonomous.chooseAngle(startSide, 250.0)));

    // stop now to let the drivers take over!

    // (first thing they should do is release the hatch panel)
  }
}
