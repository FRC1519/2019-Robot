/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import sun.net.www.content.text.plain;

/**
 * Add your docs here.
 */

public class Targeting extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  // COMPETITION ROBOT NEEDS THE ONE BELOW!
  // private double Y_WHEN_TARGET_AT_WALL = 0.65; // Worked fine and 0.70

  // PRACTICE ROBOT NEEDS THE ONE BELOW!
  // private double m_YWhenTargetAtWall= 0.75; // Worked fine and 0.70

  // Y when hatch panel is at wall when the arm is low
  private static final double Y_AT_WALL_SAFETY_MARGIN = 0.05;

  private static final double Y_WHEN_HATCH_LOW_AT_WALL = 0.75 + Y_AT_WALL_SAFETY_MARGIN;
  // Y when hatch panel is at wall when the arm is high
  private static final double Y_WHEN_HATCH_MID_AT_WALL = 0.50 + Y_AT_WALL_SAFETY_MARGIN;
  // Y when hatch panel is at wall when the arm is high
  private static final double Y_WHEN_HATCH_HIGH_AT_WALL = 0.60 + Y_AT_WALL_SAFETY_MARGIN;

  private static final double SPEED_EQ_M = -4.115;
  private static final double SPEED_EQ_B = 2.244;

  // Below values are for centered arm
  // private static final double CENTER_EQ_M = -0.1925;
  // private static final double CENTER_EQ_B = 0.5719;

  // Below values are for arm off 2 inches to left on the practice robot
  // private static final double CENTER_EQ_M_HP = -0.2964;
  // private static final double CENTER_EQ_B_HP = 0.5871;
  // private static final double CENTER_EQ_M_CARGO = -0.6102;
  // private static final double CENTER_EQ_B_CARGO = 0.6334;

  // Below values are for competition robot based upon PineTree data
  private static final double CENTER_EQ_M_HP = -0.2762;
  private static final double CENTER_EQ_B_HP = 0.5563;

  // below values are guesses for competition robot; same as on practice robot
  private static final double CENTER_EQ_M_CARGO = -0.6102;
  private static final double CENTER_EQ_B_CARGO = 0.6334;

  // After computing a desired heading, add a "fudge" offset to correct
  // empirically measured error. Found to be approx -1 degree (to shift aim 1" to
  // the left) during NECMP Thursday AM practice field session, for competition
  // robot.

  // heading correction offset had been 0.0 for PineTree
  // changed to -1.0 for first 6 matches of NECMP
  // changed to 0.0 at lunch time on Friday
  private static final double HEADING_CORRECTION_OFFSET = -0.0; // was -2.0 at CMP

  private double m_desiredHeading;
  private double[] m_target_array;
  private int m_priorFrameCount;
  private double m_priorFrameTime;
  private double[] ARRAY_OF_NEG_ONE = { -1.0 };
  private final static double FOV_CAMERA_IN_DEGREES = 78.0;
  private double m_bestY = 0.0;
  private double m_bestX = 0.0;

  public enum TargetPosition {
    LEFT_MOST, CENTER_MOST, RIGHT_MOST, CENTER_OF_RIGHT_CARGO_SHIP, CENTER_OF_LEFT_CARGO_SHIP
  };

  public enum TargetHeight {
    CARGO, HATCH
  };

  private TargetPosition m_mode = TargetPosition.CENTER_MOST;
  // Mode for target height
  private TargetHeight m_TargetHeightMode = TargetHeight.HATCH;

  // TODO: make an updateSmartDashboard() method in Targeting for optimization
  // TODO: clean up the content in Targeting.update() -- just too long!
  public void update() {
    // perform periodic update functions for the targeting capability
    int latestFrameCount = (int) SmartDashboard.getNumber("frameCount", -1.0 /* default to -1 */);
    if (latestFrameCount < 0) {
      // an invalid number for latestFrameCount, display warning light
      SmartDashboard.putBoolean("visionOK", false);
      SmartDashboard.putString("visionOkDebug", "No Data");
    } else if (latestFrameCount == m_priorFrameCount) {
      // have not received a new frame. If more than 1 second has elapsed since
      // prior new frame, display a warning light on the SmartDashboard
      if (Timer.getFPGATimestamp() > m_priorFrameTime + 1.0) {
        SmartDashboard.putBoolean("visionOK", false);
        SmartDashboard.putString("visionOkDebug", "Stale Data");
      } else {
        // else, have an old frame, but it's not too old yet, so do nothing
      }
    } else {
      // have received a new frame, save the time and update m_priorFrameCount
      m_priorFrameTime = Timer.getFPGATimestamp();
      m_priorFrameCount = latestFrameCount;
      SmartDashboard.putBoolean("visionOK", true);
      SmartDashboard.putString("visionOkDebug", "Good Data");
    }

    double[] centerMostTargetArray;
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
      m_bestX = 0.0;
      m_bestY = 0.0;
      m_desiredHeading = Robot.drive.getHeadingForCapturedImage();
    } else if (m_target_array[0] < 0.0) {
      // this means the array has no valid data. Set m_xError = 0.0
      m_bestX = 0.0;
      m_bestY = 0.0;
      m_desiredHeading = Robot.drive.getHeadingForCapturedImage();
    } else {
      // We have a valid data array.
      // There are three different situations:
      // a - We want the left-most target
      // b - We want the "centered" target
      // c - We want the right-most target

      // Handle each of them separately;
      // we need the results in "bestXError" and "bestY"

      if (m_TargetHeightMode == TargetHeight.HATCH) {
        switch (m_mode) {
        case LEFT_MOST: {
          SmartDashboard.putString("Targeting Mode", "LEFT_MOST");
          // Case A: (we want to use the left-most target)
          m_bestX = m_target_array[0]; // get the x-value
          m_bestY = m_target_array[1]; // get the y-value
          // Set m_desiredHeading
          m_desiredHeading = findDesiredHeading(m_bestX, m_bestY, m_TargetHeightMode);
          break;
        }
        case CENTER_MOST: {
          SmartDashboard.putString("Targeting Mode", "CENTER_MOST");
          // Case B:
          // Find the centermost target
          centerMostTargetArray = findTheCenterMostTarget(m_TargetHeightMode);
          m_desiredHeading = centerMostTargetArray[0];
          m_bestX = centerMostTargetArray[1];
          m_bestY = centerMostTargetArray[2];
          break;
        }
        case RIGHT_MOST: {
          SmartDashboard.putString("Targeting Mode", "RIGHT_MOST");

          // Case C

          // use "length trick" to find the last pair of points
          m_bestX = m_target_array[m_target_array.length - 2];
          m_bestY = m_target_array[m_target_array.length - 1];
          // Set m_desiredHeading
          m_desiredHeading = findDesiredHeading(m_bestX, m_bestY, m_TargetHeightMode);
          break;
        }
        case CENTER_OF_RIGHT_CARGO_SHIP: {
          SmartDashboard.putString("Targeting Mode", "CENTER_OF_RIGHT_SHIP");

          // If we see at least 3 targets
          if (6 <= m_target_array.length) {
            // Use the second target from the left
            m_bestX = m_target_array[2];
            m_bestY = m_target_array[3];
            // Set m_desiredHeading
            m_desiredHeading = findDesiredHeading(m_bestX, m_bestY, m_TargetHeightMode);
            // If we are trying to drive too much down field drive straight at the cargo
            // ship.
            // We don't want to go into the opposing side in auto!
            // if (m_desiredHeading > -60) {
            // m_desiredHeading = -90;
            // }

          } else {
            // Find the centermost target
            centerMostTargetArray = findTheCenterMostTarget(m_TargetHeightMode);
            m_desiredHeading = centerMostTargetArray[0];
            m_bestX = centerMostTargetArray[1];
            m_bestY = centerMostTargetArray[2];
          }
          break;
        }
        case CENTER_OF_LEFT_CARGO_SHIP: {
          SmartDashboard.putString("Targeting Mode", "CENTER_OF_LEFT_SHIP");

          // If we see at least 3 targets
          if (6 <= m_target_array.length) {
            // Use the second target from the Right
            m_bestX = m_target_array[m_target_array.length - 4];
            m_bestY = m_target_array[m_target_array.length - 3];
            // Set m_desiredHeading
            m_desiredHeading = findDesiredHeading(m_bestX, m_bestY, m_TargetHeightMode);
            // If we are trying to drive too much down field drive strait at the cargo ship.
            // We don't want to go into the opposing side in auto!
            // if (m_desiredHeading > 60) {
            // m_desiredHeading = 90;
            // }

          } else {
            // Find the centermost target
            centerMostTargetArray = findTheCenterMostTarget(m_TargetHeightMode);
            m_desiredHeading = centerMostTargetArray[0];
            m_bestX = centerMostTargetArray[1];
            m_bestY = centerMostTargetArray[2];
          }
          break;
        }
        default: {
          // If something goes bad set varables to default values
          DriverStation.reportError("Invalid TargetPosition in Targeting.update(): " + m_mode + "\n", false);
          m_bestY = 0.0;
          m_bestX = 0.0;
          m_desiredHeading = Robot.drive.getHeadingForCapturedImage();
          break;
        }
        }
      } else {
        // must be looking for a cargo target, which is the "highest" target
        SmartDashboard.putString("Targeting Mode", "HIGHEST");

        int i = 0;
        double tempX = 0.0;
        double tempY = 0.0;
        double bestX = 1.0;
        double bestY = 1.0;

        for (double a : m_target_array) {
          // check for invalid data
          if (a < 0.0 || a > 1.0) {
            // this should never happen. Print an error if it does.
            DriverStation.reportError("Invalid Data in target array (" + a + ") \n", false);
          } else if ((i % 2) == 0) { // true for data elements 0, 2, 4, etc.
            // this is the x value
            tempX = a;
            // if y
          } else { // this is data element 1, 3, 5, etc.
            tempY = a;

            // now that we have an x, y pair, determine if this is
            // the highest target (higest target has smallest y value)

            if (tempY < bestY) {
              bestX = tempX;
              bestY = tempY;
            }
          }
          i++;
        }
        m_desiredHeading = findDesiredHeading(bestX, bestY, m_TargetHeightMode);
      }
    }

    // at this point in the code, the "selected" target should be in the "best"
    SmartDashboard.putNumber("m_bestX", m_bestX);
    SmartDashboard.putNumber("m_bestY", m_bestY);
  }

  public double desiredHeading() {
    return m_desiredHeading + HEADING_CORRECTION_OFFSET;
  }

  public double getRecommendedSpeed() {
    // Returns a speed based upon the Y value
    double speed;

    if (m_bestY < 0.1) {
      // can't see the target, set speed to something real slow
      speed = 0.2;
    } else {

      // use the equation to determine the speed from m_bestY
      speed = (SPEED_EQ_M * m_bestY) + SPEED_EQ_B;

      // enforce min speed of 0.30 and max speed of 0.90
      if (speed < 0.30) {
        speed = 0.30;
      } else if (speed > 0.90) {
        speed = 0.90;
      }
    }

    return speed;
  }

  public boolean atWall(Autonomous.RocketHeight desiredHeight) {
    // we are at the wall when the target is lower in the field of view (bigger Y)
    // than the "at the wall" threshold
    switch (desiredHeight) {
    case HIGH:
      return (m_bestY >= Y_WHEN_HATCH_HIGH_AT_WALL);
    case MID:
      return (m_bestY >= Y_WHEN_HATCH_MID_AT_WALL);
    case LOW:
      return (m_bestY >= Y_WHEN_HATCH_LOW_AT_WALL);
    default:
      return (m_bestY >= Y_WHEN_HATCH_LOW_AT_WALL);
    }
  }

  public void setMode(TargetPosition modeToSet) {
    // Set the mode e.g. LEFT_MOST, CENTER_MOST, RIGHT_MOST,
    // CENTER_OF_RIGHT_CARGO_SHIP, CENTER_OF_LEFT_CARGO_SHIP
    m_mode = modeToSet;
  }

  public double findDesiredHeading(double X, double Y, TargetHeight target) {
    // Calulate angle error based on an X,Y
    double trueAngleError;
    double TrueCenter;
    double XError;
    double desiredHeading;
    // calculate the true center as a function of the height
    if (target == TargetHeight.CARGO) {
      TrueCenter = (CENTER_EQ_M_CARGO * Y) + CENTER_EQ_B_CARGO;
    } else {
      TrueCenter = (CENTER_EQ_M_HP * Y) + CENTER_EQ_B_HP;
    }
    // compute the "x error" based upon the trueCenter
    XError = X - TrueCenter;
    // Find the angle error
    trueAngleError = FOV_CAMERA_IN_DEGREES * XError;
    // Convert angleError into a desired heading, using the heading history
    desiredHeading = trueAngleError + Robot.drive.getHeadingForCapturedImage();
    // Update SmartDashboard
    SmartDashboard.putNumber("True Angle Error", trueAngleError);
    SmartDashboard.putNumber("Vision Desired Heading", desiredHeading);
    return desiredHeading;
  }

  public void setTargetHeight(TargetHeight target) {
    m_TargetHeightMode = target;
  }

  private double[] findTheCenterMostTarget(TargetHeight target) {
    double bestXError = 1.0;
    double tempTrueCenter = 0.0;
    int i = 0;
    double tempX = 0.0;
    double tempY = 0.0;
    double tempXError = 0.0;
    double desiredHeading;
    double bestX = 0.0;
    double bestY = 0.0;
    // Look through the valid data in the array to find the
    // target closest to the "trueCenter"

    for (double a : m_target_array) {
      // check for invalid data
      if (a < 0.0 || a > 1.0) {
        // this should never happen. Print an error if it does.
        DriverStation.reportError("Invalid Data in target array (" + a + ") \n", false);
      } else if ((i % 2) == 0) { // true for data elements 0, 2, 4, etc.
        // this is the x value
        tempX = a;
        // if y
      } else { // this is data element 1, 3, 5, etc.
        tempY = a;

        // now that we have an x, y pair, determine if this is
        // the target nearest to the "dynamic center"

        // calculate the true center as a function of the height
        if (target == TargetHeight.CARGO) {
          tempTrueCenter = (CENTER_EQ_M_CARGO * tempY) + CENTER_EQ_B_CARGO;
        } else {
          tempTrueCenter = (CENTER_EQ_M_HP * tempY) + CENTER_EQ_B_HP;
        }

        // compute the "x error" based upon the trueCenter
        tempXError = tempX - tempTrueCenter;

        if (Math.abs(tempXError) < Math.abs(bestXError)) {
          bestXError = tempXError;
          bestX = tempX;
          bestY = tempY;
        }
      }
      i++;
    }
    // Find the Desired heading baised on the x, y
    desiredHeading = findDesiredHeading(bestX, bestY, m_TargetHeightMode);
    // Create the array to return
    double[] returnArray = { desiredHeading, bestX, bestY };
    return returnArray;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
