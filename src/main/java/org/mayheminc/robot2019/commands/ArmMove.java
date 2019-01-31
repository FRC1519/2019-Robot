/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmMove extends CommandGroup {
  /**
   * Move the shoulder and wrist at the same time. The command is done when both
   * the wrist and shoulder are done moving.
   */
  public ArmMove(int shoulder, int wrist) {
    addParallel(new WristSetPosition(wrist));
    addSequential(new ShoulderSetPosition(shoulder));
  }
}
