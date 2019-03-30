/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */

public class Targeting extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private double TARGET_ALIGNED = 0.4;
  private double CENTER_EQ_M = 0.1925;
  private double CENTER_EQ_B = 0.5719;
  private double m_x_raw;
  private double m_y_raw;
  private double m_x_Error;
  private double m_trueCenter;
  private double m_angleError;
  private double m_trueAngleError;

  private final static double FOV_CAMEAR_DEGRE = 78;

  public void update() {
    m_x_raw = SmartDashboard.getNumber("targetX", -1);
    m_y_raw = SmartDashboard.getNumber("targetY", -1);
    SmartDashboard.putNumber("amountToTurn", amountToTurn());

    // Calculate ture center and x error
    if (m_x_raw <= -1.0) {
      m_x_Error = 0;
    } else {
      // Calculate the true center using the height
      m_trueCenter = (CENTER_EQ_M * m_y_raw) + CENTER_EQ_B;
      m_x_Error = m_x_raw - m_trueCenter;
      // Find the angle error
      m_angleError = FOV_CAMEAR_DEGRE * m_x_Error;
      // Find true angle error using the history
      m_trueAngleError = m_angleError + Robot.drive.getHeadingForCapturedImage();
    }

  }

  public double amountToTurn() {
    return m_x_Error;
  }

  public double centerFromHight() {
    // Calculate the true center from height
    return m_trueCenter;
  }

  public double angleError() {
    return m_angleError;
  }

  public double desiredHeading() {
    return m_trueAngleError;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
