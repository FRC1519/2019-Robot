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

public class RocketFrontToLoadingStationFast extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public RocketFrontToLoadingStationFast(Autonomous.StartOn startSide) {

    addCommands(
        // drive straight backwards for six inches to get free of hatch
        new DriveStraightOnHeading(-0.5, 12, Autonomous.chooseAngle(startSide, 38.0)),

        // drive backwards and turn to face towards the loading station
        new DriveStraightOnHeading(-0.4, 48, Autonomous.chooseAngle(startSide, 180.0)),

        // put arm and HP intake into the loading station position
        new ParallelCommandGroup(new HatchPanelLoadingStation(), new SequentialCommandGroup(
            // drive forwards towards the loading station to complete the turn
            new DriveStraightOnHeading(0.8, 36, Autonomous.chooseAngle(startSide, 180.0)),

            // // Use "AutoAlign" for the last couple seconds to get to the hatch
            new AutoAlignUntilAtWall(0.45, 3.0, Targeting.TargetPosition.CENTER_MOST)) // endSCG
        ), // endPCG

        // grab the hatch panel and wait a fraction of a second for the pneumatics to
        // move
        new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED), // get it!
        new Wait(0.2),

        // at this point, have placed Hatch Panel on the front of the rocket
        new ParallelCommandGroup(
            // in parallel, proclaim status and back away
            new PrintAutonomousTimeRemaining("Grabbed Hatch Panel"),
            // back up a little bit to pull the hatch panel from the wall
            new DriveStraightOnHeading(-0.8, 12, Autonomous.chooseAngle(startSide, 180.0))) // end ParallelCommandGroup
    );

  }
}
