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

public class HAB2HPtoShipSideBackwards extends SequentialCommandGroup {
        /**
         * Add your docs here.
         */
        public HAB2HPtoShipSideBackwards(Autonomous.StartOn startSide) {
                addCommands(
                                // Ensure High Gear and Zero the Gyro at the start of autonomous
                                new ParallelCommandGroup(
                                                // set low gear and zero the gyro in parallel
                                                new DriveSetShifter(Shifter.HIGH_GEAR), //
                                                new ZeroGyro(180.0) //
                                ),
                                // Drive straight backwards off hab level 2
                                new DriveStraightOnHeading(-0.7, 72, Autonomous.chooseAngle(startSide, 180.0)),

                                // Head for the cargo ship, by taking a long diagonal backwards path until
                                // beyond the rocket -- approx 25 feet of driving distance.

                                // distance was 294 inches and 205 degrees at practice field when working
                                // well before final speedup.
                                // last trials at practice field before NECMP were 264 and 200.0
                                // was 288 before CMP
                                new DriveStraightOnHeading(-0.8, 284, Autonomous.chooseAngle(startSide, 200.0)),

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
                                                // A - turn on the cargo intake to free the wrist from the velcro wrist
                                                // retainer
                                                new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5),

                                                // B - Move the arm to the desired position
                                                new HatchPanelLow(),

                                                // C - drive to the side of the cargo ship (first with heading control,
                                                // then
                                                // with
                                                // auto-alignment)
                                                new SequentialCommandGroup(
                                                                // Turn towards the side of the cargo ship; 270 degrees
                                                                // is perfect "in theory",
                                                                // but we need to aim to overshoot the target angle a
                                                                // bit to get there quickly.
                                                                new DriveStraightOnHeading(-0.4, 48,
                                                                                Autonomous.chooseAngle(startSide,
                                                                                                ((startSide == StartOn.RIGHT)
                                                                                                                ? 270.0
                                                                                                                : 260.0))),

                                                                // addSequential(new DriveStraightOnHeading(0.8, 24,
                                                                // Autonomous.chooseAngle(startSide, 270.0)));
                                                                new DriveStraightOnHeading(0.2, 8,
                                                                                Autonomous.chooseAngle(startSide,
                                                                                                ((startSide == StartOn.RIGHT)
                                                                                                                ? 270.0
                                                                                                                : 260.0))),
                                                                new DriveStraightOnHeading(0.5, 8,
                                                                                Autonomous.chooseAngle(startSide,
                                                                                                ((startSide == StartOn.RIGHT)
                                                                                                                ? 270.0
                                                                                                                : 270.0))),

                                                                // Use "AutoAlign" to drive to the hatch; first for
                                                                // time, then until at wall
                                                                new AutoAlignForTime(0.35, 0.7,
                                                                                ((startSide == StartOn.RIGHT)
                                                                                                ? TargetPosition.CENTER_OF_RIGHT_CARGO_SHIP
                                                                                                : TargetPosition.CENTER_OF_LEFT_CARGO_SHIP)),

                                                                new AutoAlignUntilAtWall(0.30, 1.8,
                                                                                ((startSide == StartOn.RIGHT)
                                                                                                ? TargetPosition.CENTER_OF_RIGHT_CARGO_SHIP
                                                                                                : TargetPosition.CENTER_OF_LEFT_CARGO_SHIP))) // endSCG
                                ), // end PCG

                                // release the hatch panel
                                new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED), new Wait(0.3),

                                // at this point, have placed Hatch Panel on the side of the cargo ship
                                new ParallelCommandGroup(
                                                // in parallel, proclaim victory and go get a hatch panel
                                                new PrintAutonomousTimeRemaining("Placed HP #1"), // proclaim status
                                                new ShipSideToLoadingStationFast(startSide)), // end
                                                                                              // ParallelCommandGroup

                                // now run the routine to deliver a hatch panel on to the cargo ship
                                new LoadingStationToCargoShipFast(startSide));
        }
}
