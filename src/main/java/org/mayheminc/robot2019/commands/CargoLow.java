/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.subsystems.Shoulder;
import org.mayheminc.robot2019.subsystems.Targeting;
import org.mayheminc.robot2019.subsystems.TargetingLights;
import org.mayheminc.robot2019.subsystems.Wrist;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class CargoLow extends ParallelCommandGroup {
  /**
   * Add your docs here.
   */
  public CargoLow() {
    addCommands(
        // turn off the targeting lights while getting cargo
        new TargetingLightsSet(TargetingLights.LIGHTS_OFF),

        // move the arm to the position to score cargo low
        new ArmMove(Shoulder.CARGO_ROCKET_LOW_ANGLE, Wrist.CARGO_ROCKET_LOW_ANGLE, Targeting.TargetHeight.CARGO));
  }
}
