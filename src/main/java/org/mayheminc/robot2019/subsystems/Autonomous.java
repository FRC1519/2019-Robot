/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mayheminc.robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.mayheminc.robot2019.autonomousroutines.*;

/**
 *
 * @author Team1519
 */
public class Autonomous extends Subsystem {

	private static Command autonomousPrograms[] = {

			/* 0 */ new StayStill(), // do nothing
			/* 1 */ new StraightOffHAB2(), // drive straight off HAB2 platform and stop
			/* 2 */ new StartLeftHAB2HPtoRocketFront(), // drive off level two with HP for Rocket Front
			/* 3 */ new StartLeftHAB2HPtoShipSide(), // drive off level two, with HP for Left Side of Ship
			/* 4 */ new StartLeftHAB2HPtoShipSideBackwards(), // BACK off level two, with HP for Left Side of Ship
			/* 5 */ new StartLeftHAB2HPtoShipFront(), // drive off level two, with HP for Front of Ship
			/* 6 */ new StartRightHAB2HPtoShipFront(), // drive off level two, with HP for Front of Ship
			/* 7 */ new StartRightHAB2HPtoShipSideBackwards(), // BACK off level two, with HP for Right Side of Ship
			/* 8 */ new StartRightHAB2HPtoShipSide(), // drive off level two, with HP for Right Side of Ship
			/* 9 */ new StartRightHAB2HPtoRocketFront(), // drive off level two with HP for Rocket Front
			/* 10 */ new TestAutoAlign() // This is just to test auto align.
	};

	private static int programNumber = 0; // 0 = Do nothing
	private static int delay = 0;

	public enum StartOn {
		RIGHT, LEFT
	};

	/**
	 * Convert a right-sided angle to either left or right.
	 * 
	 * @param startSide RIGHT or LEFT
	 * @param angle     0 - 360 for the right side
	 * @return
	 */
	public static double chooseAngle(StartOn startSide, double angle) {

		// if startSide is left, convert angle, otherwise, leave as-is
		if (startSide == StartOn.LEFT) {
			angle = 360.0 - angle;
		}
		return (angle);
	}

	public Autonomous() {
	}

	public void initDefaultCommand() {
	}

	public Command getSelectedProgram() {
		return autonomousPrograms[programNumber];
	}

	public int getDelay() {
		return delay;
	}

	public void adjustProgramNumber(int delta) {
		programNumber += delta;
		if (programNumber < 0) {
			programNumber = autonomousPrograms.length - 1;
		} else if (programNumber >= autonomousPrograms.length) {
			programNumber = 0;
		}
		updateSmartDashboard();
	}

	private static final int MAX_DELAY = 9;

	public void adjustDelay(int delta) {
		delay += delta;
		if (delay < 0) {
			delay = 0;
		} else if (delay > MAX_DELAY) {
			delay = MAX_DELAY;
		}
		updateSmartDashboard();
	}

	private static StringBuffer sb = new StringBuffer();

	public static void updateSmartDashboard() {
		sb.setLength(0);
		sb.append(programNumber + " " + autonomousPrograms[programNumber].getName());
		sb.append("         ");
		SmartDashboard.putString("Auto Prog", sb.toString());
		SmartDashboard.putNumber("Auto Delay", delay);
	}

	public String toString() {
		return autonomousPrograms[programNumber].getName();
	}
}
