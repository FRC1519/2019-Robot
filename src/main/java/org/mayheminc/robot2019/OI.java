
package org.mayheminc.robot2019;

import org.mayheminc.util.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import org.mayheminc.robot2019.commands.*;
import org.mayheminc.robot2019.subsystems.*;
import org.mayheminc.robot2019.subsystems.Targeting.TargetPosition;

/**
 * This class binds the controls on the physical operator interface to the
 * commands and command groups that allow control of the robot.
 */
public class OI {

	// driver pad and stick
	public static final Joystick DRIVER_PAD = new Joystick(RobotMap.DRIVER_GAMEPAD);
	public static final Joystick DRIVER_STICK = new Joystick(RobotMap.DRIVER_JOYSTICK);

	// driver stick buttons
	private static final Button DRIVER_STICK_BUTTON_ONE_DISABLED = new DisabledOnlyJoystickButton(DRIVER_STICK, 1);
	private static final Button DRIVER_STICK_BUTTON_ONE_ENABLED = new EnabledOnlyJoystickButton(DRIVER_STICK, 1);
	private static final Button DRIVER_STICK_BUTTON_TWO = new DisabledOnlyJoystickButton(DRIVER_STICK, 2);
	private static final Button DRIVER_STICK_BUTTON_THREE = new DisabledOnlyJoystickButton(DRIVER_STICK, 3);
	private static final Button DRIVER_STICK_BUTTON_FOUR = new DisabledOnlyJoystickButton(DRIVER_STICK, 4);
	private static final Button DRIVER_STICK_BUTTON_FIVE = new DisabledOnlyJoystickButton(DRIVER_STICK, 5);
	// private static final Button DRIVER_STICK_BUTTON_SIX = new
	// DisabledOnlyJoystickButton(DRIVER_STICK, 6);
	private static final Button DRIVER_STICK_BUTTON_SEVEN = new DisabledOnlyJoystickButton(DRIVER_STICK, 7);
	// private static final Button DRIVER_STICK_BUTTON_EIGHT = new
	// DisabledOnlyJoystickButton(DRIVER_STICK, 8);
	// private static final Button DRIVER_STICK_BUTTON_NINE = new
	// JoystickButton(DRIVER_STICK, 9);
	// private static final Button DRIVER_STICK_BUTTON_TEN = new
	// DisabledOnlyJoystickButton(DRIVER_STICK, 10);
	// private static final Button DRIVER_STICK_BUTTON_ELEVEN = new
	// DisabledOnlyJoystickButton(DRIVER_STICK, 11);
	public static final int DRIVER_STICK_X_AXIS = 0;
	public static final int DRIVER_STICK_Y_AXIS = 1;

	// operator pad
	public static final Joystick OPERATOR_PAD = new Joystick(RobotMap.OPERATOR_GAMEPAD);
	private static final Button OPERATOR_PAD_BUTTON_ONE = new JoystickButton(OPERATOR_PAD, 1);
	private static final Button OPERATOR_PAD_BUTTON_TWO = new JoystickButton(OPERATOR_PAD, 2);
	private static final Button OPERATOR_PAD_BUTTON_THREE = new JoystickButton(OPERATOR_PAD, 3);
	private static final Button OPERATOR_PAD_BUTTON_FOUR = new JoystickButton(OPERATOR_PAD, 4);
	private static final Button OPERATOR_PAD_BUTTON_FIVE = new JoystickButton(OPERATOR_PAD, 5);
	private static final Button OPERATOR_PAD_BUTTON_SIX = new JoystickButton(OPERATOR_PAD, 6);
	private static final Button OPERATOR_PAD_BUTTON_SEVEN = new JoystickButton(OPERATOR_PAD, 7);
	private static final Button OPERATOR_PAD_BUTTON_EIGHT = new JoystickButton(OPERATOR_PAD, 8);
	private static final Button OPERATOR_PAD_BUTTON_NINE = new JoystickButton(OPERATOR_PAD, 9);
	private static final Button OPERATOR_PAD_BUTTON_TEN = new JoystickButton(OPERATOR_PAD, 10);
	private static final Button OPERATOR_PAD_BUTTON_ELEVEN = new JoystickButton(OPERATOR_PAD, 11);
	private static final Button OPERATOR_PAD_BUTTON_TWELVE = new JoystickButton(OPERATOR_PAD, 12);

	public static final int OPERATOR_PAD_LEFT_X_AXIS = 0;
	public static final int OPERATOR_PAD_LEFT_Y_AXIS = 1;
	public static final int OPERATOR_PAD_RIGHT_X_AXIS = 2;
	public static final int OPERATOR_PAD_RIGHT_Y_AXIS = 3;

	// Operator Control Buttons from joystick movements
	@SuppressWarnings("unused")
	private static final JoystickAxisButton OPERATOR_PAD_LEFT_Y_AXIS_UP = new JoystickAxisButton(OPERATOR_PAD,
			OPERATOR_PAD_LEFT_Y_AXIS, JoystickAxisButton.NEGATIVE_ONLY);
	@SuppressWarnings("unused")
	private static final JoystickAxisButton OPERATOR_PAD_LEFT_Y_AXIS_DOWN = new JoystickAxisButton(OPERATOR_PAD,
			OPERATOR_PAD_LEFT_Y_AXIS, JoystickAxisButton.POSITIVE_ONLY);
	@SuppressWarnings("unused")
	private static final JoystickAxisButton OPERATOR_PAD_RIGHT_Y_AXIS_UP = new JoystickAxisButton(OPERATOR_PAD,
			OPERATOR_PAD_RIGHT_Y_AXIS, JoystickAxisButton.NEGATIVE_ONLY);
	@SuppressWarnings("unused")
	private static final JoystickAxisButton OPERATOR_PAD_RIGHT_Y_AXIS_DOWN = new JoystickAxisButton(OPERATOR_PAD,
			OPERATOR_PAD_RIGHT_Y_AXIS, JoystickAxisButton.POSITIVE_ONLY);

	// Axis Definitions for the F310 gamepad
	// Axis 0 - Left X Axis (-1.0 left to +1.0 right)
	// Axis 1 - Left Y Axis (-1.0 forward to +1.0 backward)
	// Axis 2 - Left Trigger (0.0 unpressed to +1.0 fully pressed)
	// Axis 3 - Right Trigger (0.0 unpressed to +1.0 fully pressed)
	// Axis 4 - Right X Axis (-1.0 left to +1.0 right)
	// Axis 5 - Right Y axis (-1.0 forward to +1.0 backward)
	// Empirical testing on 23 Jan 2015 shows that +/-0.07 is a reasonable threshold
	// for "centered"
	// (in other words, intentional non-zero values will have magnitude >= 0.07;
	// values with a magnitude of < 0.07 should probably be treated as zero.)
	public static final int GAMEPAD_F310_LEFT_X_AXIS = 0;
	public static final int GAMEPAD_F310_LEFT_Y_AXIS = 1;
	public static final int GAMEPAD_F310_LEFT_TRIGGER = 2;
	public static final int GAMEPAD_F310_RIGHT_TRIGGER = 3;
	public static final int GAMEPAD_F310_RIGHT_X_AXIS = 4;
	public static final int GAMEPAD_F310_RIGHT_Y_AXIS = 5;

	public static final int GAMEPAD_F310_A_BUTTON = 1;
	public static final int GAMEPAD_F310_B_BUTTON = 2;
	public static final int GAMEPAD_F310_X_BUTTON = 3;
	public static final int GAMEPAD_F310_Y_BUTTON = 4;
	public static final int GAMEPAD_F310_LEFT_BUTTON = 5;
	public static final int GAMEPAD_F310_RIGHT_BUTTON = 6;
	public static final int GAMEPAD_F310_BACK_BUTTON = 7;
	public static final int GAMEPAD_F310_START_BUTTON = 8;
	public static final int GAMEPAD_F310_LEFT_STICK_BUTTON = 9;
	public static final int GAMEPAD_F310_RIGHT_STICK_BUTTON = 10;

	// NOTE: DRIVER_PAD_RIGHT_UPPER_TRIGGER_BUTTON is "QUICKTURN" in Drive.java - DO
	// NOT USE HERE!!!
	private static final Button DRIVER_PAD_LEFT_UPPER_TRIGGER_BUTTON = new EnabledOnlyJoystickButton(DRIVER_PAD,
			GAMEPAD_F310_LEFT_BUTTON);
	private static final JoystickAxisButton DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD,
			GAMEPAD_F310_LEFT_TRIGGER, JoystickAxisButton.POSITIVE_ONLY);
	private static final JoystickAxisButton DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON = new JoystickAxisButton(DRIVER_PAD,
			GAMEPAD_F310_RIGHT_TRIGGER, JoystickAxisButton.POSITIVE_ONLY);

	private static final Button DRIVER_PAD_BACK_BUTTON = new JoystickButton(DRIVER_PAD, GAMEPAD_F310_BACK_BUTTON);
	private static final Button DRIVER_PAD_START_BUTTON = new JoystickButton(DRIVER_PAD, GAMEPAD_F310_START_BUTTON);
	private static final Button DRIVER_PAD_GREEN_BUTTON = new JoystickButton(DRIVER_PAD, 1); // Green "A" button
	private static final Button DRIVER_PAD_RED_BUTTON = new JoystickButton(DRIVER_PAD, 2); // RED 'B" button
	private static final Button DRIVER_PAD_BLUE_BUTTON = new JoystickButton(DRIVER_PAD, 3); // BLUE 'X' button
	private static final Button DRIVER_PAD_YELLOW_BUTTON = new JoystickButton(DRIVER_PAD, 4); // YELLOW 'Y' button
	@SuppressWarnings("unused")
	private static final Button DRIVER_PAD_BUTTON_FIVE = new JoystickButton(DRIVER_PAD, 5); // Left Top Trigger
	@SuppressWarnings("unused")
	private static final Button DRIVER_PAD_BUTTON_SIX = new JoystickButton(DRIVER_PAD, 6); // Right Top Trigger
	@SuppressWarnings("unused")
	private static final Button DRIVER_PAD_LEFT_STICK_BUTTON = new JoystickButton(DRIVER_PAD,
			GAMEPAD_F310_LEFT_STICK_BUTTON); // Left Stick Trigger
	private static final Button DRIVER_PAD_RIGHT_STICK_BUTTON = new JoystickButton(DRIVER_PAD,
			GAMEPAD_F310_RIGHT_STICK_BUTTON); // Right Stick Trigger

	@SuppressWarnings("unused")
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_UP = new JoystickPOVButton(DRIVER_PAD, 0);
	@SuppressWarnings("unused")
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_RIGHT = new JoystickPOVButton(DRIVER_PAD, 90);
	@SuppressWarnings("unused")
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_DOWN = new JoystickPOVButton(DRIVER_PAD, 180);
	@SuppressWarnings("unused")
	private static final JoystickPOVButton DRIVER_PAD_D_PAD_LEFT = new JoystickPOVButton(DRIVER_PAD, 270);

	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_LEFT = new JoystickPOVButton(OPERATOR_PAD, 270);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_RIGHT = new JoystickPOVButton(OPERATOR_PAD, 90);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_UP = new JoystickPOVButton(OPERATOR_PAD, 0);
	private static final JoystickPOVButton OPERATOR_PAD_D_PAD_DOWN = new JoystickPOVButton(OPERATOR_PAD, 180);

	private static final Button CLIMB_L3_BUTTON_PAIR = new AndJoystickButton(DRIVER_PAD, GAMEPAD_F310_Y_BUTTON,
			DRIVER_PAD, GAMEPAD_F310_LEFT_BUTTON); // Y=Yellow, plus left top trigger
	private static final Button CLIMB_L2_BUTTON_PAIR = new AndJoystickButton(DRIVER_PAD, GAMEPAD_F310_A_BUTTON,
			DRIVER_PAD, GAMEPAD_F310_LEFT_BUTTON); // A=Green, plus left top trigger

	private static boolean USE_PID_TUNER = false;
	public static PidTuner pidTuner = null;

	public OI() {

		DriverStation.reportError("OI constructor.\n", false);

		// ****************************** PID_TUNER
		// **************************************/

		if (USE_PID_TUNER) {
			// pidTuner = new PidTuner(DRIVER_STICK_BUTTON_SIX, DRIVER_STICK_BUTTON_SEVEN,
			// DRIVER_STICK_BUTTON_ELEVEN,
			// DRIVER_STICK_BUTTON_TEN, Robot.elevator);
		}

		// *******************************DRIVER
		// PAD**************************************

		// DRIVER_PAD_RED_BUTTON.whileHeld(new Wait(0) /* do nothing */ );
		DRIVER_PAD_BLUE_BUTTON.whileHeld(new AutoAlign(TargetPosition.CENTER_MOST));

		DRIVER_PAD_RIGHT_STICK_BUTTON.whileHeld(new AutoAlign(TargetPosition.CENTER_MOST));

		DRIVER_PAD_YELLOW_BUTTON.whenPressed(new Wait() /* AutoClimb() */ );
		DRIVER_PAD_RED_BUTTON.whileHeld(new LiftCylindersSetOnlyWhileHeld(LiftCylinders.EXTENDED));
		// DRIVER_PAD_GREEN_BUTTON.whileHeld(new AutoClimbL2());

		// below are alternate climb commands requiring two buttons to be pressed/held
		// to work as a safeguard
		CLIMB_L3_BUTTON_PAIR.whenPressed( /* new Wait(0) */ new AutoClimb());
		CLIMB_L2_BUTTON_PAIR.whileHeld( /* new Wait(0) */ new AutoClimbL2());

		DRIVER_PAD_LEFT_UPPER_TRIGGER_BUTTON.whenPressed(new Stow());
		DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON.whenPressed(new Wait() /* was HatchPanelLoadingStation() */);

		DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.whileHeld(new ShifterHoldGear(Shifter.LOW_GEAR)); // changed from
																									// LOW_GEAR for BAE
																									// Systems Demo

		DRIVER_PAD_BACK_BUTTON.whenPressed(new Wait(0));
		DRIVER_PAD_START_BUTTON.whileHeld(new LiftCylindersSetOnlyWhileHeld(LiftCylinders.EXTENDED));

		// DRIVER_PAD_YELLOW_BUTTON.whileHeld(new
		// LiftCylindersSetOnlyWhileHeld(LiftCylinders.EXTENDED));

		// ******************************* DRIVER STICK
		// ***********************************

		DRIVER_STICK_BUTTON_ONE_DISABLED.whenPressed(new SystemZeroIncludingGyro());
		DRIVER_STICK_BUTTON_ONE_ENABLED.whenPressed(new SystemZeroWithoutGyro());

		// // adjust auto parameters
		DRIVER_STICK_BUTTON_THREE.whenPressed(new SelectAutonomousProgram(1));
		DRIVER_STICK_BUTTON_TWO.whenPressed(new SelectAutonomousProgram(-1));
		DRIVER_STICK_BUTTON_FOUR.whenPressed(new SelectAutonomousDelay(-1));
		DRIVER_STICK_BUTTON_FIVE.whenPressed(new SelectAutonomousDelay(1));

		// // NOTE: buttons SIX, SEVEN, TEN, ELEVEN are reserved for PidTuner
		DRIVER_STICK_BUTTON_SEVEN.whenPressed(new TestSound());

		// // zero elements that require zeroing
		// DRIVER_STICK_BUTTON_EIGHT.whenPressed(new DriveZeroGyro());
		// DRIVER_STICK_BUTTON_NINE.whenPressed(new Wait(0));

		// *************************OPERATOR PAD*******************************

		OPERATOR_PAD_BUTTON_ONE.whenPressed(new CargoFloor());
		OPERATOR_PAD_BUTTON_TWO.whenPressed(new CargoLow());
		OPERATOR_PAD_BUTTON_THREE.whenPressed(new CargoMid());
		OPERATOR_PAD_BUTTON_FOUR.whenPressed(new CargoHigh());

		// BUTTONS FIVE AND SEVEN ARE For Operating the hatch panel
		OPERATOR_PAD_BUTTON_FIVE.whenPressed(new HatchPanelSet(HatchPanelPickUp.GRABBER_EXPANDED));
		OPERATOR_PAD_BUTTON_SEVEN.whenPressed(new HatchPanelSet(HatchPanelPickUp.GRABBER_CONTRACTED));

		OPERATOR_PAD_BUTTON_SIX.whileHeld(new CargoIntakeSet(CargoIntake.INTAKE_HARD_POWER));
		OPERATOR_PAD_BUTTON_EIGHT.whileHeld(new CargoIntakeSet(CargoIntake.OUTTAKE_SOFT_POWER));

		OPERATOR_PAD_D_PAD_LEFT.whenPressed(new HatchPanelLoadingStation());
		OPERATOR_PAD_D_PAD_DOWN.whenPressed(new HatchPanelLow());
		OPERATOR_PAD_D_PAD_RIGHT.whenPressed(new HatchPanelMid());
		OPERATOR_PAD_D_PAD_UP.whenPressed(new HatchPanelHigh());

		// OPERATOR_PAD_BUTTON_NINE.whileHeld(new LifterLift(Lifter.LIFTED_POS));
		OPERATOR_PAD_BUTTON_NINE.whenPressed(new WristReZeroLive());
		OPERATOR_PAD_BUTTON_TEN.whenPressed(new CargoShip());

		OPERATOR_PAD_BUTTON_ELEVEN.whenPressed(new Wait(0));
		OPERATOR_PAD_BUTTON_TWELVE.whenPressed(new Wait(0));

		// Uncomment any of the "blackbox" commands in order to debug the OI buttons
		// Robot.blackbox.addButton("DRIVER_PAD_BLUE_BUTTON", DRIVER_PAD_BLUE_BUTTON);
		// Robot.blackbox.addButton("DRIVER_PAD_GREEN_BUTTON", DRIVER_PAD_GREEN_BUTTON);
		// Robot.blackbox.addButton("DRIVER_PAD_RED_BUTTON", DRIVER_PAD_RED_BUTTON);
		// Robot.blackbox.addButton("DRIVER_PAD_YELLOW_BUTTON",
		// DRIVER_PAD_YELLOW_BUTTON);
		// Robot.blackbox.addButton("DRIVER_PAD_LEFT_UPPER_TRIGGER_BUTTON",
		// DRIVER_PAD_LEFT_UPPER_TRIGGER_BUTTON);
		// Robot.blackbox.addButton("DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON",
		// DRIVER_PAD_LEFT_LOWER_TRIGGER_BUTTON);

		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_ONE", OPERATOR_PAD_BUTTON_ONE);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_TWO", OPERATOR_PAD_BUTTON_TWO);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_THREE",
		// OPERATOR_PAD_BUTTON_THREE);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_FOUR",
		// OPERATOR_PAD_BUTTON_FOUR);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_FIVE",
		// OPERATOR_PAD_BUTTON_FIVE);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_SIX", OPERATOR_PAD_BUTTON_SIX);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_SEVEN",
		// OPERATOR_PAD_BUTTON_SEVEN);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_EIGHT",
		// OPERATOR_PAD_BUTTON_EIGHT);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_NINE",
		// OPERATOR_PAD_BUTTON_NINE);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_TEN", OPERATOR_PAD_BUTTON_TEN);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_ELEVEN",
		// OPERATOR_PAD_BUTTON_ELEVEN);
		// Robot.blackbox.addButton("OPERATOR_PAD_BUTTON_TWELVE",
		// OPERATOR_PAD_BUTTON_TWELVE);
		// Robot.blackbox.addButton("OPERATOR_PAD_D_PAD_LEFT", OPERATOR_PAD_D_PAD_LEFT);
		// Robot.blackbox.addButton("OPERATOR_PAD_D_PAD_RIGHT",
		// OPERATOR_PAD_D_PAD_RIGHT);
		// Robot.blackbox.addButton("OPERATOR_PAD_D_PAD_UP", OPERATOR_PAD_D_PAD_UP);
		// Robot.blackbox.addButton("OPERATOR_PAD_D_PAD_DOWN", OPERATOR_PAD_D_PAD_DOWN);

	}

	private static final double ARM_DEAD_ZONE_PERCENT = 0.15;

	private double applyLinearizedDeadZone(double value, double deadZonePercent) {
		// if the value is within the deadZone, make it 0.0
		if (Math.abs(value) < deadZonePercent) {
			value = 0.0;
		} else if (value > deadZonePercent) {
			// if it is above deadZonePercent, subtract the deadZonePercent to keep the
			// linearness.
			value = value - deadZonePercent;
		} else { // (this means value < -deadZonePercent)
			// if it is below deadZonePercent, add the deadZonePercent to keep the
			// linearness.
			value = value + deadZonePercent;
		}
		return value;
	}

	// This is for the "Y" axis of the Operator Gamepad.
	// However, the joysticks give -1.0 on the Y axis when pushed forward
	// This method reverses that, so that positive numbers are forward
	public double getOperatorRightY() {
		double value = -OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_Y_AXIS); // NOTE: Don't overlook the negation!
		return applyLinearizedDeadZone(value, ARM_DEAD_ZONE_PERCENT);
	}

	public double getOperatorRightX() {
		double value = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_X_AXIS);
		return applyLinearizedDeadZone(value, ARM_DEAD_ZONE_PERCENT);
	}

	// This is for the "Y" axis of the Operator Gamepad.
	// However, the joysticks give -1.0 on the Y axis when pushed forward
	// This method reverses that, so that positive numbers are forward
	public double getOperatorLeftY() {
		double value = -OPERATOR_PAD.getRawAxis(OPERATOR_PAD_LEFT_Y_AXIS); // NOTE: Don't overlook the negation!
		return applyLinearizedDeadZone(value, ARM_DEAD_ZONE_PERCENT);
	}

	public double getOperatorLeftX() {
		double value = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_LEFT_X_AXIS);
		return applyLinearizedDeadZone(value, ARM_DEAD_ZONE_PERCENT);
	}

	public double getDriverStickY() {
		double value = -DRIVER_STICK.getRawAxis(DRIVER_STICK_Y_AXIS); // NOTE: Don't overlook the negation!
		return applyLinearizedDeadZone(value, 0.15);
	}

	public double getDriverStickX() {
		double value = DRIVER_STICK.getRawAxis(DRIVER_STICK_X_AXIS);
		return applyLinearizedDeadZone(value, 0.15);
	}

	public boolean quickTurn() {
		return (DRIVER_PAD.getRawButton(OI.GAMEPAD_F310_RIGHT_BUTTON));
	}

	private static final double THROTTLE_DEAD_ZONE_PERCENT = 0.05;
	private static final double STEERING_DEAD_ZONE_PERCENT = 0.05;

	public double driveThrottle() {
		// the driveThrottle is the "Y" axis of the Driver Gamepad.
		// However, the joysticks give -1.0 on the Y axis when pushed forward
		// This method reverses that, so that positive numbers are forward
		double throttleVal = -DRIVER_PAD.getY();

		if (Math.abs(throttleVal) < THROTTLE_DEAD_ZONE_PERCENT) {
			throttleVal = 0.0;
		}

		// if the slow button is pressed, cut the throttle value in third.
		// if (DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get()) {
		// throttleVal = throttleVal / 3.0;
		// }

		return (throttleVal);
	}

	public double tankDriveLeft() {
		double tankDriveLeftAxis = -DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_LEFT_Y_AXIS);

		if (Math.abs(tankDriveLeftAxis) < THROTTLE_DEAD_ZONE_PERCENT) {
			tankDriveLeftAxis = 0.0;
		}
		return tankDriveLeftAxis;
	}

	public double tankDriveRight() {
		double tankDriveRightAxis = -DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_Y_AXIS);

		if (Math.abs(tankDriveRightAxis) < THROTTLE_DEAD_ZONE_PERCENT) {
			tankDriveRightAxis = 0.0;
		}
		return tankDriveRightAxis;
	}

	public double steeringX() {
		// SteeringX is the "X" axis of the right stick on the Driver Gamepad.
		double value = DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_X_AXIS);
		if (Math.abs(value) < STEERING_DEAD_ZONE_PERCENT) {
			value = 0.0;
		}

		// if the slow button is pressed, cut the steering value in half.
		// if (DRIVER_PAD_RIGHT_LOWER_TRIGGER_BUTTON.get()) {
		// value = value / 2.0;
		// }
		return value;
	}

	public double steeringY() {
		// However, the joysticks give -1.0 on that axis when pushed forward
		// This method reverses that, so that positive numbers are forward
		return -DRIVER_PAD.getRawAxis(OI.GAMEPAD_F310_RIGHT_Y_AXIS); // NOTE: Don't overlook the negation!
	}

	// returns true if any of the autoInTeleop buttons are held
	public boolean autoInTeleop() {
		return (DRIVER_PAD_GREEN_BUTTON.get() // added for 2019 week 1
		);
	}

	// public double getManualPower() {

	// public boolean getTurretFieldOrientedIsCommanded() {
	// double x = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_X_AXIS);
	// double y = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_Y_AXIS) * -1; // up is
	// positive

	// double mag = Math.sqrt(x * x + y * y);

	// return mag > 0.5;
	// }

	// public double getTurretFieldOrientedDirection() {
	// double x = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_X_AXIS);
	// double y = OPERATOR_PAD.getRawAxis(OPERATOR_PAD_RIGHT_Y_AXIS) * -1; // up is
	// positive

	// double radians = Math.atan2(x, y);
	// double degrees = radians * 180 / Math.PI;
	// return degrees;
	// }
}
