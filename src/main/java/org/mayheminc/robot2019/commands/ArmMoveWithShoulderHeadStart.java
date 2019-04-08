/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.subsystems.Wrist;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmMoveWithShoulderHeadStart extends CommandGroup {

  /**
   * Move the shoulder and the wrist simultaneously, but with giving the shoulder
   * head start. The command is done when both the wrist and shoulder are done
   * moving.
   * 
   * @param targetShoulderAngle Target angle for shoulder position in degrees,
   *                            from "world" perspective
   * @param targetWristAngle    Target angle for wrist position in degrees, from
   *                            "world" perspective
   * @param headStartInSecs     Duration of a head start for the shoulder, in
   *                            seconds.
   */
  public ArmMoveWithShoulderHeadStart(double targetShoulderAngle, double targetWristAngle, double headStartInSecs) {
    addParallel(new ShoulderSetAngle(targetShoulderAngle));

    addSequential(new Wait(headStartInSecs));
    addSequential(new WristSetInternalAngle(Wrist.computeInternalAngle(targetShoulderAngle, targetWristAngle)));
  }
}
