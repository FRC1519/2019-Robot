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

public class HAB2HPtoRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoRocketFront(Autonomous.StartOn startSide) {

    // Zero the Gyro at the start of autonomous
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2
    addSequential(new DriveStraightOnHeading(0.9, 60, Autonomous.chooseAngle(startSide, 0.0)));

    // Head to the right of the rocket to get onto the rocket "scoring line"
    addSequential(new DriveStraightOnHeading(0.9, 108, Autonomous.chooseAngle(startSide, 120.0)));

    // Get the arm into postion while heading for the rocket
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));

    // turn to point towards the rocket
    addSequential(new DriveStraightOnHeading(0.9, 36, Autonomous.chooseAngle(startSide, 20.0)));

    // should now turn on auto-targeting and put the HP on the rocket

    // Turn towards the side of the cargo ship; -90 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to end up on track.
    // addSequential(new DriveStraightOnHeading(0.6, 66, -110));

    // stop now to let the drivers take over!

    // (first thing they should do is release the hatch panel)
  }
}
