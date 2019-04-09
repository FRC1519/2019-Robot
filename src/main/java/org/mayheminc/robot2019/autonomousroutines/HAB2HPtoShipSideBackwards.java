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

        // Get the arm into position while heading downfield alongside the cargo ship
        addParallel(new HatchPanelLow());
        addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));

        // Head for the cargo ship, by taking a long diagonal backwards path until
        // beyond the rocket
        // guessing at 15 feet of driving distance.
        addSequential(new DriveStraightOnHeading(-0.8, 270, Autonomous.chooseAngle(startSide, 200.0)));

        // go back into low gear for the sharper turns and auto alignment
        addParallel(new DriveSetShifter(Shifter.LOW_GEAR));
        // addSequential(new Wait(2.0));

        // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
        // but we need to aim to overshoot the target angle a bit to get there quickly.

        addSequential(new DriveStraightOnHeading(-0.4, 48, Autonomous.chooseAngle(startSide, 270.0)));

        addSequential(new DriveStraightOnHeading(0.8, 24, Autonomous.chooseAngle(startSide, 270.0)));

        // addSequential(new Wait(2.0));

        // Use "AutoAlign" at 40% speed for the last 1.5 seconds to drive to the hatch
        addSequential(new AutoAlignUntilAtWall(0.5, 2.0,
                ((startSide == StartOn.RIGHT) ? TargetPosition.CENTER_MOST : TargetPosition.CENTER_MOST)));

        // release the hatch panel
        addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
        addSequential(new Wait(0.0));
        addParallel(new PrintAutonomousTimeRemaining("Placed HP #1"));

        // now run the routine to get a hatch panel from the loading station
        addSequential(new ShipSideToLoadingStationFast(startSide));

    }
}
