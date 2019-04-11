/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.AutoAlignForTime;
import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.CargoIntakeSetForTime;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoadingStationToCargoShipFast extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LoadingStationToCargoShipFast(Autonomous.StartOn startSide) {

    // Get the arm into position while heading downfield alongside the cargo ship
    addParallel(new HatchPanelLow());
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 0.5));

    // Head for the cargo ship, by taking a long diagonal backwards path until
    // beyond the rocket guessing at 15 feet of driving distance.
    // angle was 170.0 before NECMP
    addSequential(new DriveStraightOnHeading(-0.8, 256, Autonomous.chooseAngle(startSide, 167.0)));

    // was 84.0 inches for "normal batteries"
    addSequential(new DriveStraightOnHeading(-0.8, 78 - 12, Autonomous.chooseAngle(startSide, 180.0)));

    // go back into low gear for the sharper turns and auto alignment
    addParallel(new DriveSetShifter(Shifter.HIGH_GEAR)); // had previously been LOW_GEAR

    // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to get there quickly.
    addSequential(new DriveStraightOnHeading(-0.4, 48, Autonomous.chooseAngle(startSide, 270.0)));

    // addSequential(new DriveStraightOnHeading(0.8, 24,
    // Autonomous.chooseAngle(startSide, 270.0)));
    addSequential(new DriveStraightOnHeading(0.2, 8, Autonomous.chooseAngle(startSide, 270.0)));
    addSequential(new DriveStraightOnHeading(0.6 - 0.2, 16, Autonomous.chooseAngle(startSide, 270.0)));

    // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
    addSequential(
        new AutoAlignForTime(0.5, 0.7, ((startSide == StartOn.RIGHT) ? TargetPosition.CENTER_OF_RIGHT_CARGO_SHIP
            : TargetPosition.CENTER_OF_LEFT_CARGO_SHIP)));

    addSequential(new AutoAlignUntilAtWall(0.35, 1.8,
        ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST)));

    // release the hatch panel
    // addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
    // addSequential(new Wait(0.2));
    addSequential(new PrintAutonomousTimeRemaining("Placed HP #2"));

    // drive straight backwards for about a foot to get free of hatch on cargo ship
    // addSequential(new DriveStraightOnHeading(-0.8, 6,
    // Autonomous.chooseAngle(startSide, 270.0)));
  }
}
