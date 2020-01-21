/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class ArmMoveSimultaneous extends ParallelCommandGroup {
  /**
   * Move the shoulder and wrist at the same time. The command is done when both
   * the wrist and shoulder are done moving.
   * 
   * @param targetShoulderAngle Target angle for shoulder position in degrees,
   *                            from "world" perspective
   * @param targetWristAngle    Target angle for wrist position in degrees, from
   *                            "world" perspective
   */
  public ArmMoveSimultaneous(double targetShoulderAngle, double targetWristAngle) {
    addCommands(
        // move the shoulder and wrist simultaneously (in ParallelCommandGroup)
        new ShoulderSetAngle(targetShoulderAngle),
        new WristSetInternalAngle(Wrist.computeInternalAngle(targetShoulderAngle, targetWristAngle)));
  }
}
