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

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RocketFrontToLoadingStationFast extends CommandGroup {
  /**
   * Add your docs here.
   */
  public RocketFrontToLoadingStationFast(Autonomous.StartOn startSide) {

    // drive straight backwards for six inches to get free of hatch
    addSequential(new DriveStraightOnHeading(-0.5, 12, Autonomous.chooseAngle(startSide, 38.0)));

    // drive backwards and turn to face towards the loading station
    addSequential(new DriveStraightOnHeading(-0.4, 42, Autonomous.chooseAngle(startSide, 180.0)));

    // put arm and HP intake into the loading station position
    addParallel(new HatchPanelLoadingStation());
    // drive forwards towards the loading station to complete the turn
    addSequential(new DriveStraightOnHeading(0.8, 36, Autonomous.chooseAngle(startSide, 180.0)));

    // // Use "AutoAlign" for the last couple seconds to get to the hatch
    addSequential(new AutoAlignUntilAtWall(0.4, 3.0, Targeting.TargetPosition.CENTER_MOST));

    // grab the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED));

    addSequential(new Wait(0.2));
    addParallel(new PrintAutonomousTimeRemaining("Grabbed Hatch Panel"));

    // back up a little bit to pull the hatch panel from the wall
    addSequential(new DriveStraightOnHeading(-0.8, 12, Autonomous.chooseAngle(startSide, 180.0)));

  }
}
