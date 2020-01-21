/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

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
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoShipSide extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoShipSide(Autonomous.StartOn startSide) {

    addCommands(
        // Ensure Low Gear and Zero the Gyro at the start of autonomous
        new ParallelCommandGroup(
            // set low gear and zero the gyro in parallel
            new DriveSetShifter(Shifter.LOW_GEAR), //
            new ZeroGyro(0.0) //
        ),

        // Drive straight forward off hab level 2
        new DriveStraightOnHeading(0.9, 72, Autonomous.chooseAngle(startSide, 0.0)),

        // Head for the cargo ship, but mostly by aiming at the rocket for now
        // NOTE: Heading was 40.0 as of 2 April 2019

        // below was 84" on first day of Pine Tree. Increased to 88" after Q49.
        new DriveStraightOnHeading(0.7, 88, Autonomous.chooseAngle(startSide, 60.0)),

        // Next, simultaneously do three things:
        // A - free the wrist from the "secured" location on the velcro wrist retainer
        // B - move the arm into position for scoring
        // C - drive to the rocket (first with heading control, then with
        // auto-alignment)
        new ParallelCommandGroup(
            // A - turn on the cargo intake to free the wrist from the velcro wrist retainer
            new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5),

            // B - Move the arm to the desired position
            new HatchPanelLow(),

            // C - drive to the side of the cargo ship (first with heading control, then
            // with
            // auto-alignment)
            new SequentialCommandGroup(

                // below distance was 150.0 before Pine Tree; was 174.0 for first practice match
                // changed to 180 inches after first practice match; changed to 183 inches after
                // q41.
                // at practice field before NECMP changed to 130.0
                new DriveStraightOnHeading(0.7, 183, Autonomous.chooseAngle(startSide, 0.0)),

                // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
                // but we need to aim to overshoot the target angle a bit to get there quickly.
                new DriveStraightOnHeading(0.6, 48, Autonomous.chooseAngle(startSide, 250.0)),

                // Use "AutoAlign" at half speed for the last second to drive to the hatch
                new AutoAlignUntilAtWall(0.4, 1.5,
                    ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST))) // endSCG
        ), // end PCG

        // release the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED),
        // and wait for the robot to stabilize
        new Wait(0.3),

        // at this point, have placed Hatch Panel on the side of the cargo ship
        new ParallelCommandGroup(
            // in parallel, proclaim victory and go get a hatch panel
            new PrintAutonomousTimeRemaining("Placed HP #1"), // proclaim status
            new ShipSideToLoadingStation(startSide)) // end ParallelCommandGroup
    );

  }
}
