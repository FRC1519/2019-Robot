/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import org.mayheminc.robot2019.subsystems.Autonomous;

public class StartRightHAB2HPtoShipFront extends SequentialCommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoShipFront() {

    // Call the shared HAB2HPtoShipFront routine, specifying our starting side
    addCommands(new HAB2HPtoShipFront(Autonomous.StartOn.RIGHT));
  }
}
