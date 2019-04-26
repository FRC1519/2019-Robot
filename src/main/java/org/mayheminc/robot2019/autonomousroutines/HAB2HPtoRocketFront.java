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
import org.mayheminc.robot2019.commands.HatchPanelHigh;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoRocketFront(Autonomous.StartOn startSide, Autonomous.RocketHeight desiredHeight) {
    // Ensure High Gear and Zero the Gyro at the start of autonomous
    addParallel(new DriveSetShifter(Shifter.HIGH_GEAR));

    // NOTE: Starting position is placed as far forward on HAB L2 as possible,
    // pointed directly at the rocket. Front right wheel should basically be in the
    // corner of L2.
    addSequential(new ZeroGyro(Autonomous.chooseAngle(startSide, 30.0)));

    // Drive off the hab level 2
    addSequential(new DriveStraightOnHeading(0.7, 30, Autonomous.chooseAngle(startSide, 30.0))); // was 72.0

    // Get the arm into postion while heading for the rocket
    if (desiredHeight == Autonomous.RocketHeight.HIGH) {
      addParallel(new HatchPanelHigh());
    } else {
      addParallel(new HatchPanelLow());
    }
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5));

    addSequential(new DriveStraightOnHeading(0.7, 30, Autonomous.chooseAngle(startSide, 30.0))); // was 72.0

    // should now turn on auto-targeting and put the HP on the rocket
    // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
    addSequential(new AutoAlignForTime(0.35, 0.7,
        (startSide == StartOn.RIGHT) ? TargetPosition.RIGHT_MOST : TargetPosition.LEFT_MOST));
    addSequential(new AutoAlignUntilAtWall(0.30, 2.5,
        (startSide == StartOn.RIGHT) ? TargetPosition.RIGHT_MOST : TargetPosition.LEFT_MOST, desiredHeight));

    // release the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
    addSequential(new Wait(0.3));
    addParallel(new PrintAutonomousTimeRemaining("Placed HP #1"));

    // at this point, have placed Hatch Panel on the front of the rocket, back away
    // a tiny bit.
    addSequential(new DriveStraightOnHeading(-0.7, 8, Autonomous.chooseAngle(startSide, 30.0))); // was 72.0

  }
}
