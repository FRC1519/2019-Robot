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
  private double CENTER_EQ_A = 0.1925;
  private double CENTER_EQ_B = 0.5719;
  private double x_raw;
  private double y_raw;

  public void update() {
    double x_raw = SmartDashboard.getNumber("targetX", -1);
    double y_raw = SmartDashboard.getNumber("targetY", -1);
    SmartDashboard.putNumber("amountToTurn", amountToTurn());

  }

  public double amountToTurn() {
    double offSetOfX = x_raw - centerFromHight();
    if (x_raw <= -1.0) {
      offSetOfX = 0;
    }
    return offSetOfX;
  }

  public double centerFromHight() {
    // Calculate the true center from height
    return (CENTER_EQ_A * y_raw) + CENTER_EQ_B;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
