/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.subsystems.Autonomous;

public class StartRightHAB2HPtoRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoRocketFront() {

    // Call the shared HAB2HPtoRocketFront routine, specifying our starting side
    addSequential(new HAB2HPtoRocketFront(Autonomous.StartOn.RIGHT));
  }
}