/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.subsystems.Autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShipSideToLoadingStation extends CommandGroup {
  /**
   * Add your docs here.
   */
  public ShipSideToLoadingStation(Autonomous.StartOn startSide) {
    // addSequential(new ZeroGyro(-90.0));

    // drive backwards and turn to face towards the loading station
    addSequential(new DriveStraightOnHeading(-0.8, 50, Autonomous.chooseAngle(startSide, 190.0)));

    // drive forwards towards the loading station at low speed to enable sharp turn
    addSequential(new DriveStraightOnHeading(0.6, 20, Autonomous.chooseAngle(startSide, 160.0)));

    // drive to just in front of the loading station at higher speed
    addSequential(new DriveStraightOnHeading(0.9, 210, Autonomous.chooseAngle(startSide, 160.0)));

    // straighten out for the last few feet and coast into the loading station
    addSequential(new DriveStraightOnHeading(0.9, 180, Autonomous.chooseAngle(startSide, 180.0)));

  }
}
