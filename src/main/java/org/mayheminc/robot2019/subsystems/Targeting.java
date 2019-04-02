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
  // Below valls are for centered arm
  // private double CENTER_EQ_M = -0.1925;
  // private double CENTER_EQ_B = 0.5719;
  // Below valls are for arm off 2 inches to left
  private double CENTER_EQ_M = -0.2964;
  private double CENTER_EQ_B = 0.5871;
  private double m_x_raw;
  private double m_y_raw;
  private double m_x_Error;
  private double m_trueCenter;
  private double m_angleError;
  private double m_trueAngleError;
  private double m_desiredHeading;
  private double[] m_x_raw_array;
  private double errorNow = 1;
  private double xNow;
  private double yNow;
  private double x_raw;
  private double y_raw;
  private double x_Error;

  private final static double FOV_CAMEAR_DEGRE = 78;

  public void update() {
    m_x_raw_array = SmartDashboard.getNumberArray("target", new double[] { -1 });
    // m_x_raw = SmartDashboard.getNumber("targetX", -1);
    // m_y_raw = SmartDashboard.getNumber("targetY", -1);

    int i = 0;

    for (double a : m_x_raw_array) {
      // If we get an invalid number ignore it.
      if (a == -1) {
        m_x_Error = 2;
        // if x
      } else if (i == 0 || i % 2 == 0) {
        x_raw = a;
        // if y
      } else {
        y_raw = a;
        // calculate the true center as a function of the height
        m_trueCenter = (CENTER_EQ_M * a) + CENTER_EQ_B;
        // compute the "x error" based upon the trueCenter
        m_x_Error = x_raw - m_trueCenter;
        if (Math.abs(m_x_Error) < errorNow) {
          errorNow = m_x_Error;
          xNow = x_raw;
          yNow = y_raw;
        }
      }
    }
    m_x_raw = xNow;
    m_y_raw = yNow;

    // Find the angle error
    m_trueAngleError = FOV_CAMEAR_DEGRE * m_x_Error;

    // Convert angleError into a desired heading, using the heading history
    m_desiredHeading = m_trueAngleError + Robot.drive.getHeadingForCapturedImage();

    SmartDashboard.putNumber("True Center", m_trueCenter);
    SmartDashboard.putNumber("True Angle Error", m_trueAngleError);
    SmartDashboard.putNumber("Vision Desired Heading", m_desiredHeading);
    SmartDashboard.putNumber("amountToTurn", amountToTurn());

    // // if the x value is less than zero, we can't see the target
    // if (m_x_raw < 0.0) {
    // // can't see the target, act like we are aligned
    // m_x_Error = 0;
    // } else {
    // // we can see the target now, so

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
    return m_desiredHeading;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
