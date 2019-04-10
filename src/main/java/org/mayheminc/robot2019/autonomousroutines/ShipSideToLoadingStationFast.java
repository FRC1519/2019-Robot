/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.AutoAlignWithSlowdownUntilAtWall;
import org.mayheminc.robot2019.commands.DriveSetShifter;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Shifter;
import org.mayheminc.robot2019.subsystems.Targeting;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShipSideToLoadingStationFast extends CommandGroup {
  /**
   * Add your docs here.
   */
  public ShipSideToLoadingStationFast(Autonomous.StartOn startSide) {

    // drive straight backwards for about a foot to get free of hatch on cargo ship
    addSequential(new DriveStraightOnHeading(-0.5, 6, Autonomous.chooseAngle(startSide, 270.0)));

    // drive backwards and turn to face towards the loading station
    addSequential(new DriveStraightOnHeading(-0.4, 36, Autonomous.chooseAngle(startSide, 180.0)));

    // drive forwards towards the loading station at high speed in low gear to
    // enable sharp turn
    addSequential(new DriveStraightOnHeading(1.0, 20, Autonomous.chooseAngle(startSide, 157.0)));

    // drive to just in front of the loading station at higher speed
    addParallel(new DriveSetShifter(Shifter.HIGH_GEAR));
    addSequential(new DriveStraightOnHeading(0.9, 120, Autonomous.chooseAngle(startSide, 157.0)));

    // straighten out for the last few feet to line up with the loading station
    addSequential(new DriveStraightOnHeading(0.9, 72 /* was 180 */, Autonomous.chooseAngle(startSide, 180.0)));

    // TODO: consider modifying AutoAlign to use range to determine speed of drive,
    // or at least the deceleration

    // Use "AutoAlign" at half speed for the last couple seconds to get to the hatch
    addSequential(new AutoAlignUntilAtWall(0.6, 4.0, Targeting.TargetPosition.CENTER_MOST)); // was 0.6, 1.7 seconds

    // addSequential(new AutoAlignWithSlowdownUntilAtWall(4.0,
    // Targeting.TargetPosition.CENTER_MOST)); // was 0.6, 1.7
    // seconds

    // grab the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED));

    // TODO: shorten wait below (maybe just 0.0 seconds)?
    addSequential(new Wait(0.2));
    addParallel(new PrintAutonomousTimeRemaining("Grabbed Hatch Panel"));

    // back up a little bit to pull the hatch panel from the wall
    addSequential(new DriveStraightOnHeading(-0.8, 12, Autonomous.chooseAngle(startSide, 180.0)));

  }
}
