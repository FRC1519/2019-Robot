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
import org.mayheminc.robot2019.commands.HatchPanelHigh;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.PrintAutonomousTimeRemaining;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.CargoIntake;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Autonomous.StartOn;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LoadingStationToRocketBack extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LoadingStationToRocketBack(Autonomous.StartOn startSide, Autonomous.RocketHeight desiredHeight) {

    // Get the arm into position while heading downfield alongside the cargo ship
    // Get the arm into postion while heading for the rocket
    if (desiredHeight == Autonomous.RocketHeight.HIGH) {
      addParallel(new HatchPanelHigh());
    } else {
      addParallel(new HatchPanelLow());
    }
    addParallel(new CargoIntakeSetForTime(CargoIntake.OUTTAKE_HARD_POWER, 1.5));

    // Head for the back side of the rocket, by taking a long diagonal backwards
    // path until
    // beyond the rocket guessing at 22 feet of driving distance.
    // angle was 170.0 before NECMP
    addSequential(new DriveStraightOnHeading(-0.8, 204, Autonomous.chooseAngle(startSide, 167.0)));

    // drive straight backwards downfield as far as we dare without crossing
    // centerline
    addSequential(new DriveStraightOnHeading(-0.8, 90, Autonomous.chooseAngle(startSide, 180.0)));
    addSequential(new DriveStraightOnHeading(-0.8, 84, Autonomous.chooseAngle(startSide, 195.0)));

    // Turn towards the back of the rocket; 150 degrees is perfect "in theory",
    // but we need to aim to overshoot the target angle a bit to get there quickly.
    addSequential(new DriveStraightOnHeading(-0.4, 48, Autonomous.chooseAngle(startSide, 140.0)));

    // below was 0.20 percentVbus
    addSequential(new DriveStraightOnHeading(0.15, 8, Autonomous.chooseAngle(startSide, 150.0)));

    // Use "AutoAlign" to drive to the hatch; first for time, then until at wall
    addSequential(new AutoAlignForTime(0.15, 0.7,
        ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST)));

    addSequential(new AutoAlignUntilAtWall(0.15, 2.3,
        ((startSide == StartOn.RIGHT) ? TargetPosition.LEFT_MOST : TargetPosition.RIGHT_MOST), desiredHeight));

    // release the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));
    addSequential(new Wait(0.5));
    addSequential(new PrintAutonomousTimeRemaining("Placed HP #2"));

    // drive straight backwards for about half a foot to get free of hatch on rocket
    addSequential(new DriveStraightOnHeading(-0.8, 6, Autonomous.chooseAngle(startSide, 150.0)));
  }
}
