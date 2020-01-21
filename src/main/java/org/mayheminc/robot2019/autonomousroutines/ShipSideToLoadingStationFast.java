/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLoadingStation;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Targeting;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ShipSideToLoadingStationFast extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public ShipSideToLoadingStationFast(Autonomous.StartOn startSide) {
    addCommands(
        // drive straight backwards for about a foot to get free of hatch on cargo ship
        new DriveStraightOnHeading(-0.5, 6, Autonomous.chooseAngle(startSide, 270.0)),

        // drive backwards and turn to face towards the loading station
        // angle below had been 180.0 on practice robot before NECMP (in low gear)
        new DriveStraightOnHeading(-0.4, 36, Autonomous.chooseAngle(startSide, 157.0)),

        // simultaneously move arm into position while driving towards loading station
        new ParallelCommandGroup(
            // put arm and HP intake into the loading station position
            new HatchPanelLoadingStation(),
            // while simultaneously driving to cargo ship
            new SequentialCommandGroup(
                // drive forwards towards the loading station at high speed
                new DriveStraightOnHeading(0.9, 20, Autonomous.chooseAngle(startSide, 155.0)),

                // angle below had been 157.0 on practice robot before NECMP
                new DriveStraightOnHeading(0.9, 144, Autonomous.chooseAngle(startSide, 155.0)),

                // straighten out for the last few feet to line up with the loading station
                new DriveStraightOnHeading(0.9, 60 /* was 180 */, Autonomous.chooseAngle(startSide, 180.0)),

                // Use "AutoAlign" at half speed for the last few seconds to get to the hatch
                new AutoAlignUntilAtWall(0.6, 3.0, Targeting.TargetPosition.CENTER_MOST) // was 0.6, 1.7 seconds

            ) // endSCG
        ), // endPCG

        // grab the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED),
        // wait a couple tenths of a second for the pneumatics to extend
        new Wait(0.2),

        // now simultaneously print out a message while backing up from the wall
        new ParallelCommandGroup(
            // print out a message
            new PrintAutonomousTimeRemaining("Grabbed Hatch Panel"),

            // back up a little bit to pull the hatch panel from the wall
            new DriveStraightOnHeading(-0.8, 12, Autonomous.chooseAngle(startSide, 180.0)))

    );

  }
}
