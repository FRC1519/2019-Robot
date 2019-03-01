/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.DriveStraightOnHeading;
import org.mayheminc.robot2019.commands.HatchPanelLow;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.commands.ZeroGyro;

public class SecondLevelCargoShipHatchPanel extends CommandGroup {
  /**
   * Add your docs here.
   */
  public SecondLevelCargoShipHatchPanel() {
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
    addSequential(new ZeroGyro(0.0));
    // Drive of the hab level 2
    addSequential(new DriveStraightOnHeading(0.9, 96, 0)); // Drive 60 inches at a heading of zero degrees

    addSequential(new Wait(1.0));
    // Head for the cargo ship
    addSequential(new DriveStraightOnHeading(0.7, 48, -90)); // Drive three more feet turning left.

    // addSequential(new Wait(2.0));
    // Get the arm into postion

    addParallel(new HatchPanelLow());
    // Line up for the putting the hatch panel on the ship.
    addSequential(new DriveStraightOnHeading(0.7, 48, 0));

    // Wait for half a second to let eveything settle
    addSequential(new Wait(2.0));

    // addSequential(new );

  }
}
