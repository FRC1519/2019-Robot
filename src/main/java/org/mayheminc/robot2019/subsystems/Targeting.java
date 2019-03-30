/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */

public class Targeting extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private double TARGET_ALIGNED = 0.4;

  public double amountToTurn() {
    double x_raw = SmartDashboard.getNumber("targetX", -1);
    double offSetOfX = x_raw - TARGET_ALIGNED;
    if (x_raw <= -1.0) {
      offSetOfX = 0;
    }
    return offSetOfX;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
