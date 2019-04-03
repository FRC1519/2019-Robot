/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.AutoAlignUntilAtWall;
import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.subsystems.Autonomous;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;
import org.mayheminc.robot2019.commands.Wait;

public class ShipFrontToLoadingStation extends CommandGroup {
  /**
   * Add your docs here.
   */
  public ShipFrontToLoadingStation(Autonomous.StartOn startSide) {
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
    // Back up and start turning
    addSequential(new DriveStraightOnHeading(-0.5, 24, Autonomous.chooseAngle(startSide, 30.0)));
    // Finish the turn and drive towards the human player station
    addSequential(new DriveStraightOnHeading(0.5, 120, Autonomous.chooseAngle(startSide, 120.0)));
    // Straighten out infront of the driver station
    addSequential(new DriveStraightOnHeading(0.5, 24, 180));
    // Auto align to the hatch panel
    addSequential(new AutoAlignUntilAtWall(0.5, 3, TargetPosition.CENTER_MOST));
    // Grab the hatch panel
    addSequential(new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED));
    // Wait for the hatch panel to be picked up
    addSequential(new Wait(0.3));
  }
}
