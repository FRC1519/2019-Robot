
package org.mayheminc.robot2019;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.networktables.*;

import org.mayheminc.robot2019.commands.RunAutonomous;
import org.mayheminc.robot2019.subsystems.*;
import org.mayheminc.util.Utils;
import org.mayheminc.util.EventServer.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot /* IterativeRobot */ { // FRCWaitsForIterativeRobot
	static NetworkTable table;

	public static final boolean DEBUG = true;
	public static final boolean PRACTICE_BOT = false;

	// create commands to be invoked for autonomous and teleop
	private Command autonomousCommand;
	private Runtime runtime = Runtime.getRuntime();

	// default to not using humanDriverInAuto
	private boolean m_humanDriverInAuto = false;

	// create the robot subsystems
	public static final Compressor compressor = new Compressor();
	public static final Drive drive = new Drive();
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

	public static Targeting targeting = new Targeting();
	public static BlackBox blackbox = new BlackBox();
	public static Shifter shifter = new Shifter();
	public static Shoulder shoulder = new Shoulder();
	public static Wrist wrist = new Wrist();
	public static CargoIntake cargoIntake = new CargoIntake();
	public static HatchPanelPickUp hatchPanelPickUp = new HatchPanelPickUp();
	public static Lifter lifter = new Lifter();
	public static LiftCylinders liftCylinders = new LiftCylinders();
	public static LEDLights lights = new LEDLights();
	public static TargetingLights targetingLights = new TargetingLights();

	// allocate the "virtual" subsystems; wait to construct these until robotInit()
	public static Autonomous autonomous;
	public static OI oi;
	public static EventServer eventServer;

	// autonomous start time monitoring
	private static long autonomousStartTime;
	private static boolean printAutoElapsedTime = false;
	private static final double LOOP_TIME = 0.020; // The duration of our periodic timed loop, in seconds. 0.020 gives
													// 50 loops/sec

	public static final double BROWNOUT_VOLTAGE_LOWER_THRESHOLD = 10.0;
	public static final double BROWNOUT_VOLTAGE_UPPER_THRESHOLD = 11.0;
	public static final double BROWNOUT_REST_PERIOD = 3;
	public static boolean brownoutMode = false;

	public static final boolean UPDATE_AUTO_SETUP_FIELDS = true;
	public static final boolean DONT_UPDATE_AUTO_SETUP_FIELDS = false;

	public Robot() {
		super(LOOP_TIME); // construct a TimedRobot with a timeout of "LOOP_TIME"
	}

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	public void robotInit() {

		table = NetworkTableInstance.getDefault().getTable("datatable");

		System.out.println("robotInit");
		SmartDashboard.putString("Auto Prog", "Initializing...");

		System.out.println("About to DriverStation.reportError for 'Initializing...'");
		DriverStation.reportError("Initializing... ", false);
		System.out.println("Done with DriverStation.reportError for 'Initializing...'");

		// // Turn off LiveWindow reporting to avoid appearance of "ERROR 1 CTRE CAN
		// Receive Timeout" messages on concole.
		// // This fix is documented in section 4.7 of the CTRE PDP User's Guide:
		// // http://www.ctr-electronics.com/downloads/pdf/PDP%20User's%20Guide.pdf

		// // The CAN Receive Timeout occurs when the requested data has not been
		// received within the timeout.
		// // This is usually caused when the PDP is not connected to the CAN bus.
		// // However, with the 2018 version of WPILib, having a PDP object in robot
		// code can result in a
		// // CTRE CAN Timeout error being reported in the console/DriverStation.
		// // This seems to be a result of automatic LiveWindow behavior with the PDP
		// and can be fixed by
		// // disabling LiveWindow telemetry. LiveWindow can either be disabled for the
		// PDP object…
		// LiveWindow.disableTelemetry(pdp);
		// // … or by disabling all LiveWindow telemetry:
		LiveWindow.disableAllTelemetry();

		DriverStation.reportError("About to construct Autonomous Subsystem... \n", false);
		autonomous = new Autonomous();
		DriverStation.reportError("Done constructing Autonomous Subsystem.\n", false);

		DriverStation.reportError("About to construct OI... \n", false);
		oi = new OI();
		DriverStation.reportError("Done constructing OI.\n", false);

		eventServer = new EventServer();
		eventServer.add(new TMinusEvent("T-Minus 10", 10));
		eventServer.add(new TMinusEvent("T-Minus 5", 5));
		eventServer.add(new TMinusEvent("T-Minus 4", 4));
		eventServer.add(new TMinusEvent("T-Minus 3", 3));
		eventServer.add(new TMinusEvent("T-Minus 2", 2));
		eventServer.add(new TMinusEvent("T-Minus 1", 1));
		eventServer.start();

		// initialize the subsystems that need it
		drive.init();

		// instantiate the command used for the autonomous period
		autonomousCommand = new RunAutonomous();
		DriverStation.reportWarning("Constructed auto command.\n", false);
		SmartDashboard.putString("Auto Prog", "Done.");
		Autonomous.updateSmartDashboard();
	}

	/**
	 * This function is called during every period, AFTER the calls to
	 * "modePeriodic". Need to at least define robotPeriodic() to avoid a warning
	 * message at startup.
	 */
	public void robotPeriodic() {
		// TODO: To avoid having essential calls overlooked in "modePeriodic()" calls,
		// consider consolidating stuff here
		// examples: updateSensors, updateSmartDashboard, etc.

		// run the scheduler on every main loop so that commands execute
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called when the disabled button is hit. You can use it to
	 * reset subsystems before shutting down.
	 */
	public void disabledInit() {

		if (printAutoElapsedTime) {
			double autonomousTimeElapsed = (double) (System.currentTimeMillis() - autonomousStartTime) / 1000.0;
			DriverStation.reportError("Autonomous Time Elapsed: " + autonomousTimeElapsed + "\n", false);
			printAutoElapsedTime = false;
		}

		// kill any previously existing commands
		Scheduler.getInstance().removeAll();

		// // print the blackbox.
		// blackbox.print();

		// // reset the blackbox.
		// blackbox.reset();

		targetingLights.set(false);
	}

	public void disabledPeriodic() {
		// update all sensors in the robot
		updateSensors();

		// update Smart Dashboard, including fields for setting up autonomous operation
		// Note: Want to avoid excess CTRE calls, as they have latency of about 0.5ms
		// each.
		updateSmartDashboard(UPDATE_AUTO_SETUP_FIELDS);

		// ensure that the drive base updates its history (probably belongs in
		// "updateSensors")
		Robot.drive.updateHistory();

		// Scheduler.getInstance().run(); called in periodic().

		// for safety reasons, keep resetting the shoulder and wrist setpoints to the
		// current position, so that when we leave "disabled" no shoulder or wrist
		// motion is commanded
		shoulder.setDesiredAngle(shoulder.getAngleInDegrees());
		shoulder.relaxMotors();

		wrist.setDesiredAngle(wrist.getAngleInDegrees());
		wrist.relaxMotors();

		// show the default lights.
		lights.update();
	}

	public void autonomousInit() {

		/// establish starting robot state for autonomous on all subsystems that need it

		// zero the drive base gyro at current position
		drive.zeroHeadingGyro(0.0);

		// ensure the hatch panel grabber is "holding" the hatch panel
		hatchPanelPickUp.set(HatchPanelPickUp.GRABBER_EXPANDED);

		// TODO: do we want autonomous in high gear instead this year?
		// force low gear
		shifter.setGear(Shifter.LOW_GEAR);

		lights.setDefault(LedPatternFactory.defaultAuto);

		// turn off the compressor
		// KBS: Not sure we really want to do this -- we did this in 2016 to ensure the
		// compressor didn't affect the operation of the autonomous programs. Not sure
		// we really want this.
		// At the least, we can take advantage of the last few seconds in autonomous by
		// turning on the compressor at the end of our autonomous programs instead of
		// waiting for the teleopInit to be called at the start of teleop.
		// compressor.stop();

		// TODO: examine section below for "Zero" updates needed in 2019.
		// "Zero" the robot subsystems which have position encoders in this section.
		// Overall strategy for zeroing subsystems is as follows:
		// Every time autonomous starts:
		// "Zero" the heading gyro using the drive subsystem
		// "Zero" the elevator, presuming it is down due to gravity
		// "Zero" the turret, presuming it is pointing straight forward
		// "Zero" the arm/pivot, which will initiate finding zero by using hard stop

		// start the autonomous period without a human driver
		m_humanDriverInAuto = false;

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
		autonomousStartTime = System.currentTimeMillis();
		printAutoElapsedTime = true;

		DriverStation.reportError(
				"AutonomousTimeRemaining from autonomousInit = " + Robot.autonomousTimeRemaining() + "\n", false);

		targetingLights.set(true);
	}

	public static double autonomousTimeRemaining() {
		double autonomousTimeElapsed = (double) (System.currentTimeMillis() - autonomousStartTime) / 1000.0;
		return (15.0 - autonomousTimeElapsed);
	}

	public static double getMatchStartTime() {
		return autonomousStartTime / 1000.0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

		// update all sensors in the robot, including the targeting subsystem
		updateSensors();

		// Scheduler.getInstance().run(); called in periodic().

		// If the driver's joystick has either joystick deflected, and the autonomous
		// program is still trying to drive the robot, end the autonomous program
		// and switch control to the human driver.
		if ((oi.driveThrottle() != 0.0) || (oi.steeringX() != 0) && !m_humanDriverInAuto) {
			// ensure the autonomous command is canceled
			if (autonomousCommand != null) {
				Scheduler.getInstance().removeAll();
				// autonomousCommand.cancel();
			}
			// switch to control by a human driver
			m_humanDriverInAuto = true;
		}

		// if a humanDriver is operating the robot, call the appropriate drive method
		if (m_humanDriverInAuto) {
			drive.speedRacerDrive(oi.driveThrottle(), oi.steeringX(), oi.quickTurn());
		}

		updateSmartDashboard(DONT_UPDATE_AUTO_SETUP_FIELDS);
		drive.updateHistory();
		shoulder.update();
		wrist.update();
		lights.update();
		cargoIntake.update();
		targeting.update();
	}

	public void teleopInit() {

		// before doing anything else in teleop, kill any existing commands
		// TODO: consider if this is still desirable in 2019 with the "sandstorm" period
		Scheduler.getInstance().removeAll();

		lights.setDefault(LedPatternFactory.defaultTeleOp);

		// turn on the compressor (may have been off in autonomous)
		compressor.start();

		DriverStation.reportError("Entering Teleop.\n", false);

		shifter.setGear(Shifter.HIGH_GEAR);

		targetingLights.set(true);
	}

	/**
	 * This function is called periodically during operator control
	 */
	boolean teleopOnce = false;

	static double periodicTimer = 0;
	static int periodicLoops = 0;
	static String periodicOutputString = "periodic: ";

	void PrintPeriodicPeriod() {
		double timer = Timer.getFPGATimestamp();
		double elapsed = timer - periodicTimer;

		// add a timing figure to the printout, but only if we're too slow
		if (elapsed <= (LOOP_TIME + 0.002)) {
			periodicOutputString = periodicOutputString + "* ";
		} else {
			periodicOutputString = periodicOutputString + Utils.threeDecimalPlaces(elapsed) + " ";
		}
		// DriverStation.reportWarning("periodic: " + (timer-periodicTimer), false);
		if ((periodicLoops % 50) == 1) {
			// System.out.println("periodic: " +
			// Utils.threeDecimalPlaces(timer-periodicTimer));
			periodicOutputString = Utils.threeDecimalPlaces(timer) + " " + periodicOutputString;
			System.out.println(periodicOutputString);
			periodicOutputString = "periodic: ";
		}
		periodicLoops++;
		periodicTimer = timer;
	}

	public void teleopPeriodic() {
		PrintPeriodicPeriod();

		if (!teleopOnce) {
			DriverStation.reportError("Teleop Periodic is running!", false);
		}
		teleopOnce = true;

		// Scheduler.getInstance().run(); called in periodic

		// update all sensors in the robot
		updateSensors();

		// drive the robot based upon joystick inputs, unless an "auto" command is
		// driving
		if (!oi.autoInTeleop()) {
			drive.speedRacerDrive(oi.driveThrottle(), oi.steeringX(), oi.quickTurn());
		} else {
			// do nothing here since the autonomous code will do the driving...
		}

		updateSmartDashboard(DONT_UPDATE_AUTO_SETUP_FIELDS);

		drive.updateHistory();
		lifter.update();
		shoulder.update();
		wrist.update();
		lights.update();
		cargoIntake.update();
		targeting.update();
	}

	public static boolean getBrownoutMode() {
		return brownoutMode;
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
	}

	public void updateSensors() {
		shoulder.updateSensors();
		wrist.updateSensors();
	}

	private double SMART_DASHBOARD_UPDATE_INTERVAL = 0.250; // was 0.250;
	private double nextSmartDashboardUpdate = Timer.getFPGATimestamp();

	public void updateSmartDashboard(boolean updateAutoFields) {
		double currentTime = Timer.getFPGATimestamp();

		if (currentTime > nextSmartDashboardUpdate) {

			// System.out.println("updateSmartDashboard: current: " + currentTime + " next:
			// " + nextSmartDashboardUpdate);

			this.updateSmartDashboard();

			shifter.updateSmartDashboard();
			drive.updateSmartDashboard();
			lifter.updateSmartDashboard();
			shoulder.updateSmartDashboard();
			wrist.updateSmartDashboard();
			cargoIntake.updateSmartDashboard();
			lights.updateSmartDashboard();

			if (OI.pidTuner != null) {
				OI.pidTuner.updateSmartDashboard();
			}

			if (updateAutoFields) {
				Autonomous.updateSmartDashboard();
			}
			// TODO: Should really fix this a bit so that if we are "falling behind"
			// we don't build up a backlog.
			// On the other hand, can't just set nextSmartDashboardUpdate = currentTime +
			// SMART_DASHBOARD_UPDATE_INTERVAL
			// or the intervals will gradually drift. Need a more intelligent solution to
			// avoid both undesireable cases.
			// nextSmartDashboardUpdate += SMART_DASHBOARD_UPDATE_INTERVAL;
			nextSmartDashboardUpdate = currentTime + SMART_DASHBOARD_UPDATE_INTERVAL;
		}
	}

	void updateSmartDashboard() {
		// display free memory for the JVM
		double freeMemoryInKB = runtime.freeMemory() / 1024;
		SmartDashboard.putNumber("Free Memory", freeMemoryInKB);
	}
}
