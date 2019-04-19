/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.AutoAlignForTime;
import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.CargoIntakeSetForTime;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoRocketFront(Autonomous.StartOn startSide) {

    // Ensure High Gear and Zero the Gyro at the start of autonomous
    addParallel(new DriveSetShifter(Shifter.HIGH_GEAR));
    addSequential(new ZeroGyro(0.0));

    // Drive off the hab level 2
    addSequential(new DriveStraightOnHeading(0.7, 60, Autonomous.chooseAngle(startSide, 0.0))); // was 72.0

    // Head directly towards the rocket
    addSequential(new DriveStraightOnHeading(0.7, 72, Autonomous.chooseAngle(startSide, 60.0))); // was 108.0

    // Get the arm into postion while heading for the rocket
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));

    addSequential(new Wait(3.0));

    // should now turn on auto-targeting and put the HP on the rocket

    // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
    addSequential(new AutoAlignForTime(0.35, 0.7, TargetPosition.RIGHT_MOST));
    addSequential(new AutoAlignUntilAtWall(0.30, 1.8, TargetPosition.RIGHT_MOST));

    // release the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
    addSequential(new Wait(0.3));
    addParallel(new PrintAutonomousTimeRemaining("Placed HP #1"));

    // at this point, have placed Hatch Panel on the front of the rocket.

  }
}
