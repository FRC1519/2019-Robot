package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Wrist extends Subsystem {

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
    public static final double FRONT_ANGLE_SLOP = 0.0; // 17.0; // front angles need to be increased by 17 degrees
    public static final double HORIZONTAL_ANGLE = 0.0 + FRONT_ANGLE_SLOP;

    public static final double CARGO_ROCKET_HIGH_ANGLE = 45.0 + FRONT_ANGLE_SLOP;
    public static final double CARGO_ROCKET_MID_ANGLE = 24.0 + FRONT_ANGLE_SLOP;
    public static final double CARGO_ROCKET_LOW_ANGLE = 20.0 + FRONT_ANGLE_SLOP;
    public static final double CARGO_CARGO_SHIP_ANGLE = 3.0 + FRONT_ANGLE_SLOP;
    public static final double CARGO_FLOOR_PICKUP_ANGLE = -22.0 + FRONT_ANGLE_SLOP;
    public static final double CARGO_LOADING_STATION_ANGLE = 25.0 + FRONT_ANGLE_SLOP;

    public static final double HP_ROCKET_HIGH_ANGLE = 100.0 + FRONT_ANGLE_SLOP;
    public static final double HP_ROCKET_MID_ANGLE = 100.0 + FRONT_ANGLE_SLOP;
    public static final double HP_ROCKET_LOW_ANGLE = 95.0 + FRONT_ANGLE_SLOP;
    public static final double HP_FLOOR_PICKUP_ANGLE = 0.0 + FRONT_ANGLE_SLOP;
    public static final double HP_LOADING_STATION_ANGLE = 95.0 + FRONT_ANGLE_SLOP;

    private static final double ANGLE_TOLERANCE = 10.0;

    private final MayhemTalonSRX motor = new MayhemTalonSRX(RobotMap.WRIST_TALON);

    private int m_currentPosition = 0;
    private double m_desiredAngle = STARTING_POSITION_DEGREES;
    private int m_desiredPosition = 0; // desired position when in "Internal" mode

    private double m_internalAngleInDegrees; // current angle of wrist, relative to arm, in degrees

    private double m_angleInDegrees; // current angle of wrist, relative to ground, in degrees
    private double m_angleInRadians; // current angle of wrist, relative to ground, in radians
    private double m_gravityCompensation; // computed "Gravity Compensation" factor, based upon current angle of arm
    private double m_feedForward; // computed "Feed Forward" term, in %vbus, based upon current angle of arm

    private enum WristMode {
        MANUAL, AUTO_WORLD, AUTO_INTERNAL
    };

    private WristMode m_mode = WristMode.MANUAL; // start off in manual mode

    public Wrist() {
        // initial calcs for computing kP...
        // If we want 50% wrist power when 30 degrees from target,
        // 30 degrees is 341 ticks.
        // kP = (0.50 * 1023) / 341 = 1.50
        motor.config_kP(0, 3.0, 0); // based upon Ken's initial calcs, above

        // typical value of about 1/100 of kP for starting tuning
        motor.config_kI(0, 0.0, 0);

        // typical value of about 10x to 100x of kP for starting tuning
        motor.config_kD(0, 0.0, 0);

        // practically always set kF to 0 for position control
        // for things like gravity compensation, use the "arbitrary feed forward" that
        // can be specified with the "4-parameter" TalonSRX.set() method
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Coast);
        motor.setInverted(true);
        motor.setSensorPhase(true);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.configClosedloopRamp(0.05); // limit neutral to full to 0.10 seconds // motor direction is reversed; not
                                          // quite sure why
        // TODO: Need to set up motion magic parameters for wrist below
        motor.configMotionCruiseVelocity(100); // measured velocity of ~100K at 85%; set cruise to that
        motor.configMotionAcceleration(250); // acceleration of 2x velocity allows cruise to be attained in 1/2 second
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    public void zero() {
        // zero the position.
        motor.setSelectedSensorPosition(ZERO_POS);
        setInternalPosition(ZERO_POS);
    }

    public void relaxMotors() {
        m_mode = WristMode.MANUAL;
        motor.set(ControlMode.PercentOutput, 0.0, DemandType.ArbitraryFeedForward, 0.0);
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

    private int getCurrentPosition() {
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
        m_currentPosition = motor.getPosition();
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
        case MANUAL:
            SmartDashboard.putString("Wrist Mode", "MANUAL");
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
        SmartDashboard.putNumber("Wrist Voltage", motor.getOutputVoltage());
        SmartDashboard.putNumber("Wrist Amps A", motor.getOutputCurrent());
        // SmartDashboard.putNumber("Wrist Joystick", Robot.oi.getOperatorRightY());
        // SmartDashboard.putNumber("Wrist Velocity",
        // motor.getSelectedSensorVelocity());
    }

    // public void setManualMode(boolean b) {
    // this.m_manualMode = b;
    // }

    public void update() {

        // if the operator is moving the joystick for manual control, ensure manual mode

        if (Robot.oi.getOperatorRightY() != 0.0) {
            this.m_mode = WristMode.MANUAL;
        }

        switch (this.m_mode) {
        case MANUAL:
            motor.set(ControlMode.PercentOutput, Robot.oi.getOperatorRightY(), DemandType.ArbitraryFeedForward,
                    m_feedForward);
            break;
        case AUTO_WORLD:
            this.motor.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle), DemandType.ArbitraryFeedForward,
                    m_feedForward);
            break;
        case AUTO_INTERNAL:
            this.motor.set(ControlMode.MotionMagic, m_desiredPosition, DemandType.ArbitraryFeedForward, m_feedForward);
            break;
        default:
            break;
        }
    }
}
