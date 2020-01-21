/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ArmMoveWithWristHeadStart extends ParallelCommandGroup {
  /**
   * Move the shoulder and the wrist simultaneously, but with giving the wrist a
   * head start. The command is done when both the wrist and shoulder are done
   * moving.
   * 
   * @param targetShoulderAngle Target angle for shoulder position in degrees,
   *                            from "world" perspective
   * @param targetWristAngle    Target angle for wrist position in degrees, from
   *                            "world" perspective
   * @param headStartInSecs     Duration of a head start for the wrist, in
   *                            seconds.
   */
  public ArmMoveWithWristHeadStart(double targetShoulderAngle, double targetWristAngle, double headStartInSecs) {
    addCommands(
        // move the wrist first with a hard start before the shoulder
        // (in ParallelCommandGroup)
        new WristSetInternalAngle(Wrist.computeInternalAngle(targetShoulderAngle, targetWristAngle)),

        // move the shoulder simultaneously with the shoulder, but after a delay
        new SequentialCommandGroup( //
            new Wait(headStartInSecs), //
            new ShoulderSetAngle(targetShoulderAngle)) // end SCG
    );
  }
}
