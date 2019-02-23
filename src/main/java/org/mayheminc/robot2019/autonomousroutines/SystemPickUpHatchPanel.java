/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.autonomousroutines;

import org.mayheminc.robot2019.commands.ArmMove;
import org.mayheminc.robot2019.commands.HatchPanelSet;
import org.mayheminc.robot2019.commands.Wait;
import org.mayheminc.robot2019.subsystems.HatchPanelPickUp;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SystemPickUpHatchPanel extends CommandGroup {
  /**
   * Open the hatch graber. Lower the arm to just above the floor. rotate the
   * wrist to get the hatch panel. Grab the hatch panel. Lift the hatch panel.
   */
  public SystemPickUpHatchPanel() {
    // open (release) the hatch panel and move the arm to the 'low' position.
    addParallel(new HatchPanelSet(HatchPanelPickUp.RELEASE));
    // addSequential(new ArmMove(100, 1000));

    // move the wrist to under the robot and lower the arm a little.
    // addSequential(new ArmMove(90, 1500));

    addSequential(new HatchPanelSet(HatchPanelPickUp.GRAB));
    addSequential(new Wait(0.5));

    // lift the arm back up, careful to rotate the hatch panel from under the robot.
    // addSequential(new ArmMove(100, 1000));
  }
}
