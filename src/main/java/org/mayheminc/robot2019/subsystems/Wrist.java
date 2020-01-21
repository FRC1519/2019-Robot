package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Wrist extends SubsystemBase {

    public static final int ZERO_POS = 0;
    public static final int STARTING_POS = ZERO_POS; // the same as "zero_pos" - lowered all the way into the robot
    public static final double STARTING_POSITION_DEGREES = 180.0; // the starting position (approx) in degrees
    private static final double ENCODER_CPR = 4096.0;
    private static final double OVERALL_REDUCTION = 1.0; // no reduction after the encoder
    private static final double TICKS_PER_ROTATION = ENCODER_CPR * OVERALL_REDUCTION; // turns out to be 4096
    private static final double TICKS_PER_DEGREE = TICKS_PER_ROTATION / 360.0; // turns out to be approx 11.38
    private static final double HORIZONTAL_HOLD_OUTPUT = 0.15; // need to determine empirically
    private static final double GRAVITY_OFFSET_ANGLE_RADIANS = Math.toRadians(55.0);

    // in general, the wrist angle positions are relative to the ground
    public static final double HORIZONTAL_ANGLE = 0.0;

    public static final double CARGO_ROCKET_HIGH_ANGLE = 55.0; // was 55 before NECMP
    public static final double CARGO_ROCKET_MID_ANGLE = 50.0; // was 50 before NECMP
    public static final double CARGO_ROCKET_LOW_ANGLE = 50.0; // was 50 before NECMP
    public static final double CARGO_CARGO_SHIP_ANGLE = -7.0;
    public static final double CARGO_FLOOR_PICKUP_ANGLE = -35.0; // was -20.0 on comp robot at NECMP
    // public static final double CARGO_LOADING_STATION_ANGLE = 25.0;

    public static final double HP_ROCKET_HIGH_ANGLE = 100.0; // was 95.0, raise a bit to account for HP sag
    public static final double HP_ROCKET_MID_ANGLE = 100.0; // was 95.0, raise a bit to account for HP sag
    public static final double HP_ROCKET_LOW_ANGLE = 100.0; // was 95.0, raise a bit to account for HP sag
    public static final double HP_FLOOR_PICKUP_ANGLE = 0.0;
    public static final double HP_LOADING_STATION_ANGLE = 95.0; // was 95.0 (keep at 95, since no HP in robot)

    private static final double ANGLE_TOLERANCE = 10.0;

    private final MayhemTalonSRX m_motor = new MayhemTalonSRX(RobotMap.WRIST_TALON);

    private int m_currentPosition = 0;
    private double m_desiredAngle = STARTING_POSITION_DEGREES;
    private int m_desiredPosition = 0; // desired position when in "Internal" mode

    private double m_internalAngleInDegrees; // current angle of wrist, relative to arm, in degrees

    private double m_angleInDegrees; // current angle of wrist, relative to ground, in degrees
    private double m_angleInRadians; // current angle of wrist, relative to ground, in radians
    private double m_gravityCompensation; // computed "Gravity Compensation" factor, based upon current angle of arm
    private double m_feedForward; // computed "Feed Forward" term, in %vbus, based upon current angle of arm

    private double m_percentOutput = 0.0; // percent output to use when in robot_manual mode
    private int m_highCurrentDuration = 0;
    private boolean m_isHighCurrent = false;

    private enum WristMode {
        ROBOT_MANUAL, DRIVER, RELAXED, AUTO_WORLD, AUTO_INTERNAL
    };

    private WristMode m_mode = WristMode.DRIVER; // start off in manual mode

    public Wrist() {
        // initial calcs for computing kP...
        // If we want 50% wrist power when 30 degrees from target,
        // 30 degrees is 341 ticks.
        // kP = (0.50 * 1023) / 341 = 1.50
        m_motor.config_kP(0, 3.0, 0); // based upon Ken's initial calcs, above

        // typical value of about 1/100 of kP for starting tuning
        m_motor.config_kI(0, 0.0, 0);

        // typical value of about 10x to 100x of kP for starting tuning
        m_motor.config_kD(0, 0.0, 0);

        // practically always set kF to 0 for position control
        // for things like gravity compensation, use the "arbitrary feed forward" that
        // can be specified with the "4-parameter" TalonSRX.set() method
        m_motor.config_kF(0, 0.0, 0);

        m_motor.setNeutralMode(NeutralMode.Coast);
        m_motor.setInverted(false);
        m_motor.setSensorPhase(true);
        m_motor.configNominalOutputVoltage(+0.0f, -0.0f);
        m_motor.configPeakOutputVoltage(+12.0, -12.0);
        m_motor.configClosedloopRamp(0.05); // limit neutral to full to 0.05 seconds

        m_motor.configMotionCruiseVelocity(100); // measured velocity of ~100K at 85%; set cruise to that
        m_motor.configMotionAcceleration(250); // acceleration of 2x velocity allows cruise to be attained in 1/2 second
        m_motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    public void zero() {
        // zero the position.
        zeroWithOffset(0);
    }

    public void zeroWithOffset(int offset) {
        // zero the position.  Note that offset is not in degrees, but is in sensor counts!
        m_motor.setSelectedSensorPosition(ZERO_POS + offset);
        setInternalPosition(ZERO_POS + offset);

        m_mode = WristMode.DRIVER;
    }

    public void setPercentOutput(double percentOutput) {
        m_percentOutput = percentOutput;
        m_mode = WristMode.ROBOT_MANUAL;
    }

    public boolean isHighCurrent() {
        return m_isHighCurrent;
    }

    public void relaxMotors() {
        m_mode = WristMode.RELAXED;
        m_motor.set(ControlMode.PercentOutput, 0.0, DemandType.ArbitraryFeedForward, 0.0);
    }

    // Note that this setDesiredAngle is relative to the ground!!!
    public void setDesiredAngle(double angle) {
        m_desiredAngle = angle;
        m_mode = WristMode.AUTO_WORLD;
    }

    public static double computeInternalAngle(double shoulderAngle, double wristWorldAngle) {
        return (wristWorldAngle - shoulderAngle);
    }

    // Note that this setInternalAngle is relative to the robot arm!!!
    public void setInternalAngle(double angle) {
        m_desiredPosition = internalDegreesToPosition(angle);
        m_mode = WristMode.AUTO_INTERNAL;
    }

    // Note that this setInternalPosition is relative to the arm!!!
    private void setInternalPosition(int pos) {
        // Need to add shoulder angle to convert from internal to relative coordinates
        m_internalAngleInDegrees = positionToInternalDegrees(pos);
        m_desiredAngle = m_internalAngleInDegrees + Robot.shoulder.getAngleInDegrees();
    }

    public boolean isAtInternalSetpoint() {
        return (Math
                .abs(m_internalAngleInDegrees - positionToInternalDegrees(m_desiredPosition)) < Wrist.ANGLE_TOLERANCE);
    }

    public boolean isAtSetpoint() {
        return Math.abs(m_angleInDegrees - m_desiredAngle) < Wrist.ANGLE_TOLERANCE;
    }

    public double getAngleInDegrees() {
        return m_angleInDegrees;
    }

    public double getInternalAngleInDegrees() {
        return positionToInternalDegrees(m_currentPosition); // position converted to degrees
    }

    public int getCurrentPosition() {
        return (int) m_currentPosition;
    }

    public void initDefaultCommand() {
    }

    private double positionToInternalDegrees(int position) {
        return position / TICKS_PER_DEGREE + STARTING_POSITION_DEGREES;
    }

    private int internalDegreesToPosition(double degrees) {
        return (int) ((degrees - STARTING_POSITION_DEGREES) * TICKS_PER_DEGREE);
    }

    // converts (external) degrees to an internal position
    private int degreesToPosition(double degrees) {
        return (int) ((degrees - STARTING_POSITION_DEGREES - Robot.shoulder.getAngleInDegrees()) * TICKS_PER_DEGREE);
    }

    // updateSensors() should be called on every main loop, whether robot is
    // disabled, autonomous, or teleop
    // Update all sensor values and compute all terms which depend solely upon
    // sensor values
    public void updateSensors() {
        m_currentPosition = m_motor.getPosition();
        m_internalAngleInDegrees = positionToInternalDegrees(m_currentPosition);

        // to get "floor-relative" angles, need to add shoulder angle
        m_angleInDegrees = m_internalAngleInDegrees + Robot.shoulder.getAngleInDegrees();
        m_angleInRadians = Math.toRadians(m_angleInDegrees);

        // get a range of -1 to 1 to multiply by feedforward.
        // when in horizontal forward position, value should be 1
        // when in vertical up or down position, value should be 0
        // when in horizontal backward position, value should be -1
        m_gravityCompensation = Math.cos(m_angleInRadians - GRAVITY_OFFSET_ANGLE_RADIANS);

        // HORIZONTAL_HOLD_OUTPUT is the minimum power required to hold the arm up when
        // horizontal
        // this is a range of -1.0 to 1.0 (%vbus), determined empirically
        m_feedForward = m_gravityCompensation * HORIZONTAL_HOLD_OUTPUT;
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Wrist Current Pos", getCurrentPosition());
        SmartDashboard.putNumber("Wrist Current Degrees", m_angleInDegrees);
        switch (m_mode) {
        case ROBOT_MANUAL:
            SmartDashboard.putString("Wrist Mode", "ROBOT_MANUAL");
            break;
        case DRIVER:
            SmartDashboard.putString("Wrist Mode", "DRIVER");
            break;
        case RELAXED:
            SmartDashboard.putString("Wrist Mode", "RELAXED");
            break;
        case AUTO_WORLD:
            SmartDashboard.putString("Wrist Mode", "AUTO_WORLD");
            break;
        case AUTO_INTERNAL:
            SmartDashboard.putString("Wrist Mode", "AUTO_INTERNAL");
            break;
        }
        SmartDashboard.putNumber("Wrist Desired Degrees", m_desiredAngle);
        SmartDashboard.putNumber("Wrist Desired Position", m_desiredPosition);
        // SmartDashboard.putNumber("Wrist Gravity Compensation",
        // m_gravityCompensation);
        // SmartDashboard.putNumber("Wrist FeedForward", m_feedForward);
        SmartDashboard.putNumber("Wrist Voltage", m_motor.getOutputVoltage());
        SmartDashboard.putNumber("Wrist Amps A", m_motor.getOutputCurrent());
        // SmartDashboard.putNumber("Wrist Joystick", Robot.oi.getOperatorRightY());
        // SmartDashboard.putNumber("Wrist Velocity",
        // motor.getSelectedSensorVelocity());

        SmartDashboard.putBoolean("Wrist isLowCurrent", !m_isHighCurrent);
    }

    // public void setManualMode(boolean b) {
    // this.m_manualMode = b;
    // }

    public void update() {

        // if the operator is moving the joystick for manual control, ensure manual mode

        if (Robot.oi.getOperatorRightY() != 0.0) {
            this.m_mode = WristMode.DRIVER;
        }

        switch (this.m_mode) {
        case ROBOT_MANUAL:
            m_motor.set(ControlMode.PercentOutput, m_percentOutput, DemandType.ArbitraryFeedForward, m_feedForward);
            break;
        case DRIVER:
            m_motor.set(ControlMode.PercentOutput, Robot.oi.getOperatorRightY(), DemandType.ArbitraryFeedForward,
                    m_feedForward);
            break;
        case RELAXED:
            m_motor.set(ControlMode.PercentOutput, 0.0, DemandType.ArbitraryFeedForward, 0.0);
            break;
        case AUTO_WORLD:
            this.m_motor.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle),
                    DemandType.ArbitraryFeedForward, m_feedForward);
            break;
        case AUTO_INTERNAL:
            this.m_motor.set(ControlMode.MotionMagic, m_desiredPosition, DemandType.ArbitraryFeedForward,
                    m_feedForward);
            break;
        default:
            break;
        }

        // if the current is high...
        if (m_motor.getOutputCurrent() > 4.0) {
            // keep count of how long it has been high
            m_highCurrentDuration++;

            // if high for too long, set "isHighCurrent" to true
            if (m_highCurrentDuration >= 10) {
                m_isHighCurrent = true;
            }
        } else {
            // current not high at this moment, reset the counter
            m_highCurrentDuration = 0;
            m_isHighCurrent = false;
        }

    }
}
