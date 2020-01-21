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
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LoadingStationToCargoShipFast extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public LoadingStationToCargoShipFast(Autonomous.StartOn startSide) {

    addCommands(
        // Next, simultaneously do three things:
        // A - free the wrist from the "secured" location on the velcro wrist retainer
        // (in case it got accidentally stuck)
        // B - move the arm into position for scoring
        // C - drive to the back side of the rocket
        new ParallelCommandGroup(
            // A - turn on the cargo intake to free the wrist from the velcro wrist retainer
            new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5),

            // B - Move the arm to the desired position
            new HatchPanelLow(),

            // C - drive to the nearest bay of the cargo ship
            new SequentialCommandGroup(
                // Head for the cargo ship, by taking a long diagonal backwards path until
                // beyond the rocket guessing at 15 feet of driving distance.
                // angle was 170.0 before NECMP
                // was 256 before CMP; now 252
                new DriveStraightOnHeading(-0.8, 252, Autonomous.chooseAngle(startSide, 167.0)),

                // was 84.0 inches for "normal batteries"
                new DriveStraightOnHeading(-0.8, 78 - 12, Autonomous.chooseAngle(startSide, 180.0))

            ) // endSCG
        ), // end PCG

        // Head for the cargo ship, by taking a long diagonal backwards path until
        // beyond the rocket guessing at 15 feet of driving distance.
        // angle was 170.0 before NECMP
        // was 256 before CMP; now 252
        new DriveStraightOnHeading(-0.8, 252, Autonomous.chooseAngle(startSide, 167.0)),

        // was 84.0 inches for "normal batteries"
        new DriveStraightOnHeading(-0.8, 78 - 12, Autonomous.chooseAngle(startSide, 180.0)),

        // Turn towards the side of the cargo ship; 270 degrees is perfect "in theory",
        // but we need to aim to overshoot the target angle a bit to get there quickly.
        new DriveStraightOnHeading(-0.4, 48, Autonomous.chooseAngle(startSide, 270.0)),

        // addSequential(new DriveStraightOnHeading(0.8, 24,
        // Autonomous.chooseAngle(startSide, 270.0)));
        new DriveStraightOnHeading(0.2, 8, Autonomous.chooseAngle(startSide, 270.0)),
        new DriveStraightOnHeading(0.5, 8, Autonomous.chooseAngle(startSide, 270.0)),

        // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
        new AutoAlignForTime(0.35, 0.7,
            ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST)),

        new AutoAlignUntilAtWall(0.30, 1.8,
            ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST)),

        // release the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED), new Wait(0.3),
        new PrintAutonomousTimeRemaining("Placed HP #2"),

        // drive straight backwards for about a foot to get free of hatch on cargo ship
        new DriveStraightOnHeading(-0.8, 6, Autonomous.chooseAngle(startSide, 270.0)));
  }
}
