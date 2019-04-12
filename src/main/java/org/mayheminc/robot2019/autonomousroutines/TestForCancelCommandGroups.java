/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.Wait;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestForCancelCommandGroups extends CommandGroup {
  /**
   * Add your docs here.
   */
  public TestForCancelCommandGroups() {
    // This is a test of cancelling command groups
    addSequential(new Wait(5.0));

    addParallel(new Wait(15.1));
    addParallel(new Wait(10.2));
    addSequential(new Wait(5.3));

    addSequential(new Wait(10.4));

    addSequential(new Wait(10.5));
  }
}
