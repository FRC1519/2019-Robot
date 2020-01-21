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

public class HatchPanelMid extends ParallelCommandGroup {
  /**
   * Add your docs here.
   */
  public HatchPanelMid() {
    addCommands(
      // turn on the targeting lights while scoring a hatch panel
      new TargetingLightsSet(TargetingLights.LIGHTS_ON), 

      // move the arm to the position for hatch panel mid scoring
      new ArmMove(Shoulder.HP_ROCKET_MID_ANGLE, Wrist.HP_ROCKET_MID_ANGLE, Targeting.TargetHeight.HATCH)
      );
  }
}
