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
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

public class HAB2HPtoShipSideBackwards extends CommandGroup {
        /**
         * Add your docs here.
         */
        public HAB2HPtoShipSideBackwards(Autonomous.StartOn startSide) {

                // Ensure High Gear and Zero the Gyro at the start of autonomous
                addParallel(new DriveSetShifter(Shifter.HIGH_GEAR));
                addSequential(new ZeroGyro(180.0));

                // Drive straight backwards off hab level 2
                addSequential(new DriveStraightOnHeading(-0.7, 72, Autonomous.chooseAngle(startSide, 180.0)));

                // Rezero the wrist now that we are on the floor
                // addParallel(new WristReZeroLive());

                // Head for the cargo ship, by taking a long diagonal backwards path until
                // beyond the rocket -- approx 25 feet of driving distance.

                // distance was 294 inches and 205 degrees at practice field when working
                // well before final speedup.
                // last trials at practice field before NECMP were 264 and 200.0
                addSequential(new DriveStraightOnHeading(-0.8, 288, Autonomous.chooseAngle(startSide, 200.0)));

                // Get the arm into position while heading downfield alongside the cargo ship
                addParallel(new HatchPanelLow());
                addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5));

                // go back into low gear for the sharper turns and auto alignment
                addParallel(new DriveSetShifter(Shifter.HIGH_GEAR)); // had previously been LOW_GEAR

                // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
                // but we need to aim to overshoot the target angle a bit to get there quickly.
                addSequential(new DriveStraightOnHeading(-0.4, 48,
                                Autonomous.chooseAngle(startSide, ((startSide == StartOn.RIGHT) ? 270.0 : 260.0))));

                // addSequential(new DriveStraightOnHeading(0.8, 24,
                // Autonomous.chooseAngle(startSide, 270.0)));
                addSequential(new DriveStraightOnHeading(0.2, 8,
                                Autonomous.chooseAngle(startSide, ((startSide == StartOn.RIGHT) ? 270.0 : 260.0))));
                addSequential(new DriveStraightOnHeading(0.5, 8,
                                Autonomous.chooseAngle(startSide, ((startSide == StartOn.RIGHT) ? 270.0 : 270.0))));

                // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
                addSequential(new AutoAlignForTime(0.35, 0.7,
                                ((startSide == StartOn.RIGHT) ? TargetPosition.CENTER_OF_RIGHT_CARGO_SHIP
                                                : TargetPosition.CENTER_OF_LEFT_CARGO_SHIP)));

                addSequential(new AutoAlignUntilAtWall(0.30, 1.8,
                                ((startSide == StartOn.RIGHT) ? TargetPosition.CENTER_OF_RIGHT_CARGO_SHIP
                                                : TargetPosition.CENTER_OF_LEFT_CARGO_SHIP)));

                // release the hatch panel
                addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
                addSequential(new Wait(0.3));
                addParallel(new PrintAutonomousTimeRemaining("Placed HP #1"));

                // now run the routine to get a hatch panel from the loading station
                addSequential(new ShipSideToLoadingStationFast(startSide));

                // now run the routine to deliver a hatch panel on to the cargo ship
                addSequential(new LoadingStationToCargoShipFast(startSide));
        }
}
