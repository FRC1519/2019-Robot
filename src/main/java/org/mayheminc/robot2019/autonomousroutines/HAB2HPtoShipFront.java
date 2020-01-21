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
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoShipFront extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoShipFront(Autonomous.StartOn startSide) {
    addCommands(
        // Ensure High Gear and Zero the Gyro at the start of autonomous
        new ParallelCommandGroup(
            // set high gear and zero the gyro in parallel
            new DriveSetShifter(Shifter.HIGH_GEAR), //
            new ZeroGyro(0.0) //
        ),

        // Drive off the hab level 2 and wait a second to stop bouncing around
        new DriveStraightOnHeading(0.7, 96, Autonomous.chooseAngle(startSide, 0.0)), // Drive 60 inches at a
                                                                                     // heading of zero
                                                                                     // degrees
        new Wait(1.0),

        // Head for the cargo ship
        new DriveStraightOnHeading(0.5, 42, Autonomous.chooseAngle(startSide, 270.0)), // Drive three more
                                                                                       // feet turning left.

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

            // C - drive to the rocket (first with heading control, then with
            // auto-alignment)
            new SequentialCommandGroup(

                new DriveStraightOnHeading(0.5, 48, Autonomous.chooseAngle(startSide, 0.0)),
                new AutoAlignUntilAtWall(0.35, 2.0, TargetPosition.CENTER_MOST)) // endSCG
        ), // end PCG

        // release the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED),
        // and wait for the robot to stabilize
        new Wait(0.3),

        // at this point, have placed Hatch Panel on the front of the ship
        new ParallelCommandGroup(
            // in parallel, proclaim victory and let the drivers take over
            new PrintAutonomousTimeRemaining("Placed HP #1"), // proclaim victory
            new DriveSetShifter(Shifter.HIGH_GEAR)) // end ParallelCommandGroup
    );
  }
}
