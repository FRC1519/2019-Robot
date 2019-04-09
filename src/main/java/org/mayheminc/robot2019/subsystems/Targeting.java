/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */

public class Targeting extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private double Y_WHEN_TARGET_AT_WALL = 0.65; // Worked fine and 0.70

  // Below valls are for centered arm
  // private double CENTER_EQ_M = -0.1925;
  // private double CENTER_EQ_B = 0.5719;

  // Below values are for arm off 2 inches to left on the practice robot
  private double CENTER_EQ_M = -0.2964;
  private double CENTER_EQ_B = 0.5871;

  // Below values are for competition robot based upon PineTree data
  // private double CENTER_EQ_M = -0.2762;
  // private double CENTER_EQ_B = 0.5563;

  private double m_angleError;
  private double m_trueAngleError;
  private double m_desiredHeading;
  private double[] m_target_array;
  private double[] ARRAY_OF_NEG_ONE = { -1.0 };
  private final static double FOV_CAMERA_IN_DEGREES = 78.0;
  private double m_bestY = 0.0;
  private double m_bestX = 0.0;

  public enum TargetPosition {
    LEFT_MOST, CENTER_MOST, RIGHT_MOST
  };

  private TargetPosition m_mode = TargetPosition.CENTER_MOST;

  public void update() {
    int i = 0;
    double tempX = 0.0;
    double tempY = 0.0;
    double tempTrueCenter = 0.0;
    double tempXError = 0.0;
    double bestXError = 1.0;

    // Update all of the targeting information, as follows:
    // 1 - Determine if we have any valid data in the array.
    // If not, set the "error" to zero, so that the robot thinks
    // it is on target.
    // 2 - Look through the valid data in the array to find the
    // target closest to the "trueCenter"
    // 3 - Use the selected target to compute the needed information

    // get the latest output from the targeting camera
    m_target_array = SmartDashboard.getNumberArray("target", ARRAY_OF_NEG_ONE);

    if (m_target_array == null || m_target_array.length == 0) {
      // this means the key is found, but is empty
      bestXError = 0.0;
      m_bestX = 0.0;
      m_bestY = 0.0;
    } else if (m_target_array[0] < 0.0) {
      // this means the array has no valid data. Set m_xError = 0.0
      bestXError = 0.0;
      m_bestX = 0.0;
      m_bestY = 0.0;
    } else {
      // We have a valid data array.
      // There are three different situations:
      // a - We want the left-most target
      // b - We want the "centered" target
      // c - We want the right-most target

      // Handle each of them separately;
      // we need the results in "bestXError" and "bestY"

      if (/* want left-most */ m_mode == TargetPosition.LEFT_MOST) {
        // Case A: (we want to use the left-most target)
        m_bestY = m_target_array[1]; // get the y-value

        // calculate the true center as a function of the height
        tempTrueCenter = (CENTER_EQ_M * m_bestY) + CENTER_EQ_B;

        // compute the "x error" based upon the trueCenter
        m_bestX = m_target_array[0];
        bestXError = m_bestX - tempTrueCenter;
      } else if (/* want center */ m_mode == TargetPosition.CENTER_MOST) {
        // Case B:
        // Look through the valid data in the array to find the
        // target closest to the "trueCenter"

        for (double a : m_target_array) {
          // check for invalid data
          if (a < 0.0 || a > 1.0) {
            // this should never happen. Print an error if it does.
            DriverStation.reportError("Invalid Data in target array (" + a + ") \n", false);
          } else if (i % 2 == 0) { // true for data elements 0, 2, 4, etc.
            // this is the x value
            tempX = a;
            // if y
          } else { // this is data element 1, 3, 5, etc.
            tempY = a;

            // now that we have an x, y pair, determine if this is
            // the target nearest to the "dynamic center"

            // calculate the true center as a function of the height
            tempTrueCenter = (CENTER_EQ_M * tempY) + CENTER_EQ_B;

            // compute the "x error" based upon the trueCenter
            tempXError = tempX - tempTrueCenter;
            if (Math.abs(tempXError) < Math.abs(bestXError)) {
              bestXError = tempXError;
              m_bestX = tempX;
              m_bestY = tempY;
            }
          }
          i++;
        }
      } else if (m_mode == TargetPosition.RIGHT_MOST) {
        // Case C

        // use "length trick" to find the last pair of points
        m_bestY = m_target_array[m_target_array.length - 1];

        // calculate the true center as a function of the height
        tempTrueCenter = (CENTER_EQ_M * m_bestY) + CENTER_EQ_B;

        // compute the "x error" based upon the trueCenter
        m_bestX = m_target_array[m_target_array.length - 2];
        bestXError = m_bestX - tempTrueCenter;

      } else {
        // If something goes bad set varables to default values

        DriverStation.reportError("Invalid TargetPosition in Targeting.update(): " + m_mode + "\n", false);

        m_bestY = 0.0;
        m_bestX = 0.0;
        bestXError = 0.0;
      }
    }

    // at this point in the code, the "selected" target should be in the "best"
    // variables of "bestXError" and "m_bestY"

    // Find the angle error
    m_trueAngleError = FOV_CAMERA_IN_DEGREES * bestXError;

    // Convert angleError into a desired heading, using the heading history
    m_desiredHeading = m_trueAngleError + Robot.drive.getHeadingForCapturedImage();

    SmartDashboard.putNumber("True Angle Error", m_trueAngleError);
    SmartDashboard.putNumber("m_bestX", m_bestX);
    SmartDashboard.putNumber("m_bestY", m_bestY);
    SmartDashboard.putNumber("Vision Desired Heading", m_desiredHeading);
  }

  public double angleError() {
    return m_angleError;
  }

  public double desiredHeading() {
    return m_desiredHeading;
  }

  public boolean atWall() {
    // we are at the wall when the target is lower in the field of view (bigger Y)
    // than the "at the wall" threshold
    return (m_bestY >= Y_WHEN_TARGET_AT_WALL);
  }

  public void setMode(TargetPosition modeToSet) {
    m_mode = modeToSet;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
