/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Targeting;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ShipSideToLoadingStation extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public ShipSideToLoadingStation(Autonomous.StartOn startSide) {

    addCommands(
        // drive straight backwards for about a foot to get free of hatch on cargo ship
        new DriveStraightOnHeading(-0.4, 12, Autonomous.chooseAngle(startSide, 270.0)),

        // drive backwards and turn to face towards the loading station
        new DriveStraightOnHeading(-0.5, 50, Autonomous.chooseAngle(startSide, 190.0)),

        // drive forwards towards the loading station at low speed to enable sharp turn
        new DriveStraightOnHeading(0.5, 20, Autonomous.chooseAngle(startSide, 160.0)),

        // drive to just in front of the loading station at higher speed
        new ParallelCommandGroup(
            // switch back to high gear
            new DriveSetShifter(Shifter.HIGH_GEAR),
            // drive to just in front of the loading station
            new DriveStraightOnHeading(0.5, 180, Autonomous.chooseAngle(startSide, 160.0))), // end PCG

        // straighten out for the last few feet to line up with the loading station
        new DriveStraightOnHeading(0.5, 24 /* was 180 */, Autonomous.chooseAngle(startSide, 180.0)),

        // Note: the above leaves about 80 inches (distance had been 180) to go for
        // "auto-align" to do its thing

        // Use "AutoAlign" at half speed for the last couple seconds to get to the hatch
        new AutoAlignUntilAtWall(0.4, 5.0, Targeting.TargetPosition.CENTER_MOST), // was 0.6, 1.7 seconds

        // grab the hatch panel
        new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED), new Wait(0.5),

        // at this point, have grabbed a hatch panel from the loading station
        new ParallelCommandGroup(
            // in parallel, proclaim victory and go get a hatch panel
            new PrintAutonomousTimeRemaining("Grabbed Hatch Panel"),
            // back up a little bit to pull the hatch panel from the wall
            new DriveStraightOnHeading(-0.8, 12, Autonomous.chooseAngle(startSide, 180.0))), // end ParallelCommandGroup

        new DriveSetShifter(Shifter.HIGH_GEAR));

  }
}
