/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.subsystems.Autonomous;

public class StartRightHAB2HPtoShipSideBackwards extends CommandGroup {
  /**
   * Add your docs here.
   */
  public StartRightHAB2HPtoShipSideBackwards() {

    // Call the shared HAB2HPtoShipSideBackwards routine, specifying our starting side
    addSequential(new HAB2HPtoShipSideBackwards(Autonomous.StartOn.RIGHT));
  }
}
