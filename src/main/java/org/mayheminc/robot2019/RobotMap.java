package org.mayheminc.robot2019;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Drive CAN Talons
	public static final int LEFT_FRONT_TALON = 1;
	public static final int LEFT_REAR_TALON = 2;
	public static final int RIGHT_FRONT_TALON = 3;
	public static final int RIGHT_REAR_TALON = 4;

	// Subsystem Talons
	public static final int SHOULDER_TALON_A = 20;
	public static final int SHOULDER_TALON_B = 27;
	public static final int WRIST_TALON = 21;
	public static final int INTAKE_ROLLER_TALON = 22;
	public static final int LIFTER_LEFT_A_TALON = 23;
	public static final int LIFTER_RIGHT_A_TALON = 24;
	public static final int LIFTER_LEFT_B_TALON = 25;
	public static final int LIFTER_RIGHT_B_TALON = 26;

	// Solenoids:
	public static final int SHIFTING_SOLENOID = 0;
	public static final int HATCH_PANEL_SOLENOID = 1;
	public static final int SHOULDER_BRAKE_SOLENOID = 2;
	public static final int LIFTER_SOLENOID = 3;
	public static final int TARGETING_LIGHTS_SOLENOID = 4;

	// PWM Devices
	public static final int BLINKIN_LEDS_PWM = 0;

	// Analog Inputs

	// Joysticks
	public static final int DRIVER_GAMEPAD = 0;
	public static final int DRIVER_JOYSTICK = 1;
	public static final int OPERATOR_GAMEPAD = 2;
	public static final int OPERATOR_JOYSTICK = 3;

	// PDP Channels =
	public static final int DRIVE_FRONT_LEFT_PDP = 15;
	public static final int DRIVE_BACK_LEFT_PDP = 14;
	public static final int DRIVE_FRONT_RIGHT_PDP = 0;
	public static final int DRIVE_BACK_RIGHT_PDP = 1;
}
