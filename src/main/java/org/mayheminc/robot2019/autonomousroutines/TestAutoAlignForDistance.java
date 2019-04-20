/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.AutoAlignForDistance;
import org.mayheminc.robot2019.commands.AutoAlignForDistance.DistanceUnits;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestAutoAlignForDistance extends CommandGroup {
  /**
   * Add your docs here.
   */
  public TestAutoAlignForDistance() {
    // Test AutoAlignForDistance
    addSequential(new AutoAlignForDistance(0.35, DistanceUnits.INCHES, 24.0, 2.5, TargetPosition.CENTER_MOST));

  }
}
