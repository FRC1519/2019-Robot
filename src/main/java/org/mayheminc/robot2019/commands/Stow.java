/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.subsystems.Shoulder;
import org.mayheminc.robot2019.subsystems.Targeting;
import org.mayheminc.robot2019.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Stow extends SequentialCommandGroup {
  public Stow() {
    // the stow command is just an ArmMove to the starting position for the arm
    addCommands(
      new ArmMove(Shoulder.STARTING_POSITION_DEGREES,
        (Wrist.STARTING_POSITION_DEGREES + Shoulder.STARTING_POSITION_DEGREES), Targeting.TargetHeight.HATCH));
  }
}
