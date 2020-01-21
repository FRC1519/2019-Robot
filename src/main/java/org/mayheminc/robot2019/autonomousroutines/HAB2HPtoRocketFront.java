/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

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

public class HAB2HPtoRocketFront extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public HAB2HPtoRocketFront(Autonomous.StartOn startSide, Autonomous.RocketHeight desiredHeight) {

    // NOTE: Starting position is placed as far forward on HAB L2 as possible,
    // pointed directly at the rocket. Front right wheel should basically be in the
    // corner of L2.

    addCommands(
        // Ensure High Gear and Zero the Gyro at the start of autonomous
        new ParallelCommandGroup(
            // set high gear and zero the gyro in parallel
            new DriveSetShifter(Shifter.HIGH_GEAR), //
            new ZeroGyro(Autonomous.chooseAngle(startSide, 30.0)) //
        ),

        // Drive off the hab level 2
        new DriveStraightOnHeading(0.7, 30, Autonomous.chooseAngle(startSide, 30.0)), // was 72.0

        // Next, simultaneously do three things:
        // A - free the wrist from the "secured" location on the velcro wrist retainer
        // B - move the arm into position for scoring
        // C - drive to the rocket (first with heading control, then with
        // auto-alignment)
        new ParallelCommandGroup(
            // A - turn on the cargo intake to free the wrist from the velcro wrist retainer
            new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5),

            // B - Move the arm to the desired position
            ((desiredHeight == Autonomous.RocketHeight.HIGH) ? new HatchPanelHigh() : new HatchPanelLow()),

            // C - drive to the rocket (first with heading control, then with
            // auto-alignment)
            new SequentialCommandGroup(
                // drive on a heading for a little to stop bouncing
                new DriveStraightOnHeading(0.7, 30, Autonomous.chooseAngle(startSide, 30.0)), // was 72.0

                // should now turn on auto-targeting and put the HP on the rocket
                // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
                new AutoAlignForTime(0.40, 0.7,
                    (startSide == StartOn.RIGHT) ? TargetPosition.RIGHT_MOST : TargetPosition.LEFT_MOST),
                new AutoAlignUntilAtWall(0.35, 2.5,
                    (startSide == StartOn.RIGHT) ? TargetPosition.RIGHT_MOST : TargetPosition.LEFT_MOST, desiredHeight)) // endSCG
        ), // end PCG

        // release the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED),
        // and wait for the robot to stabilize
        new Wait(0.3),

        // at this point, have placed Hatch Panel on the front of the rocket
        new ParallelCommandGroup(
            // in parallel, proclaim victory and back away a tiny bit
            new PrintAutonomousTimeRemaining("Placed HP #1"),
            new DriveStraightOnHeading(-0.7, 8, Autonomous.chooseAngle(startSide, 30.0)) // was 72.0
        ) // end ParallelCommandGroup
    );
  }
}
