/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Add your docs here.
 */
public class HatchPanelSet extends InstantCommand {
  /**
   * Add your docs here.
   */
  boolean m_pos;

  public HatchPanelSet(boolean b) {
    super();
    // Use requires() here to declare subsystem dependencies
    requires(Robot.hatchPanelPickUp);
    m_pos = b;
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.hatchPanelPickUp.set(m_pos);
  }

}
