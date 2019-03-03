package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.mayheminc.robot2019.Robot;
import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;

public class Shoulder extends Subsystem {

    public static final int ZERO_POS = 0;
    public static final int STARTING_POS = ZERO_POS; // the same as "zero_pos" - lowered all the way into the robot
    private static final double STARTING_POSITION_DEGREES = -78.0; // the starting position (approx) in degrees
    private static final double ENCODER_CPR = 4096.0;
    private static final double OVERALL_REDUCTION = 325.0 * 48.0 / 12.0; // gearbox reduction multiplied by sprocket
                                                                         // reduction
    private static final double TICKS_PER_ROTATION = ENCODER_CPR * OVERALL_REDUCTION; // turns out to be 5,324,800
    private static final double TICKS_PER_DEGREE = TICKS_PER_ROTATION / 360.0; // turns out to be approx 14,791
    private static final double HORIZONTAL_HOLD_OUTPUT = 0.08; // found this empirically - may need to adjust based upon
                                                               // wrist position

    public static final double HORIZONTAL_ANGLE = 0.0;

    public static final double CARGO_ROCKET_HIGH_ANGLE = 66.0;
    public static final double CARGO_ROCKET_MID_ANGLE = 27.0;
    public static final double CARGO_ROCKET_LOW_ANGLE = -13.0;
    public static final double CARGO_CARGO_SHIP_ANGLE = 20.0; // (was -2.0)
    public static final double CARGO_FLOOR_PICKUP_ANGLE = -32.0;
    public static final double CARGO_LOADING_STATION_ANGLE = -10.0;

    public static final double HP_ROCKET_HIGH_ANGLE = 42.0;
    public static final double HP_ROCKET_MID_ANGLE = -6.0;
    public static final double HP_ROCKET_LOW_ANGLE = -68.0;
    public static final double HP_FLOOR_PICKUP_ANGLE = -35.0;
    public static final double HP_LOADING_STATION_ANGLE = -10.0;

    private static final double ANGLE_TOLERANCE = 3.0; // hoping for one degree!!! (very optimistic, about 0.5 inches)
    private static final double BRAKE_RELEASE_TIME_SEC = 0.0; // TODO: update with the real brake release time, once we
                                                              // are using it

    private MayhemTalonSRX motor_A = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_A);
    private MayhemTalonSRX motor_B = new MayhemTalonSRX(RobotMap.SHOULDER_TALON_B);
    private Solenoid brake = new Solenoid(RobotMap.SHOULDER_BRAKE_SOLENOID);

    private int m_currentPosition = 0;
    private double m_desiredAngle = STARTING_POSITION_DEGREES;

    enum State {
        STOPPED, START_MOVING, WAIT_FOR_BRAKE_RELEASE, MOTOR_MOVING,
    };

    private State m_state = State.STOPPED;
    private Timer m_timer = new Timer();
    private double m_startTimer;

    private double m_angleInDegrees; // current angle of arm, relative to ground, in degrees
    private double m_angleInRadians; // current angle of arm, relative to ground, in radians
    private double m_gravityCompensation; // computed "Gravity Compensation" factor, based upon current angle of arm
    private double m_feedForward; // computed "Feed Forward" term, in %vbus, based upon current angle of arm

    private boolean m_manualMode = true; // TODO: debug. Set to false for init

    public Shoulder() {
        configMotor(motor_A);
        configMotorFollower(motor_B, motor_A.getDeviceID());
    }

    void configMotor(MayhemTalonSRX motor) {
        // initial calcs for computing kP...
        // If we want 50% shoulder power when 30 degrees from target,
        // 30 degrees is 44,373 ticks.
        // kP = (0.50 * 1023) / 44,373 = 0.0115
        motor.config_kP(0, 0.0070, 0); // based upon Ken's initial calcs, above

        // typical value of about 1/100 of kP for starting tuning
        motor.config_kI(0, 0.0, 0);

        // typical value of about 10x to 100x of kP for starting tuning
        motor.config_kD(0, 0.0, 0);
        // motor.config_kD(0, 0.575, 0);

        // practically always set kF to 0 for position control
        // for things like gravity compensation, use the "arbitrary feed forward" that
        // can be specified with the "4-parameter" TalonSRX.set() method
        motor.config_kF(0, 0.0, 0);

        motor.setNeutralMode(NeutralMode.Coast);
        motor.setInverted(false);
        motor.setSensorPhase(false);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -12.0);
        motor.configClosedloopRamp(0.05); // limit neutral to full to 0.05 seconds
        motor.configMotionCruiseVelocity(100000); // measured velocity of ~100K at 85%; set cruise to that
        motor.configMotionAcceleration(200000); // acceleration of 2x velocity allows cruise to be attained in 1/2
                                                // second
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    void configMotorFollower(MayhemTalonSRX motor, int id) {
        configMotor(motor);

        motor.changeControlMode(ControlMode.Follower);
        motor.set(id);
    }

    /**
     * Call to zero the arm. Set the brake. Set the motor to the starting position.
     */
    public void zero() {
        brake.set(true); // set the brake.

        // zero the position.
        motor_A.setSelectedSensorPosition(ZERO_POS);
        setPosition(ZERO_POS);
    }

    public void setDesiredAngle(double angle) {
        m_desiredAngle = angle;
        m_state = State.START_MOVING;
        m_manualMode = false;
    }

    private void setPosition(int pos) {
        m_desiredAngle = positionToDegrees(pos);
        m_state = State.STOPPED;
    }

    public void moveToZeroPosition() {
        m_desiredAngle = positionToDegrees(ZERO_POS);
        m_state = State.STOPPED;
        m_manualMode = false;
    };

    public boolean isAtSetpoint() {
        return Math.abs(m_angleInDegrees - m_desiredAngle) < Shoulder.ANGLE_TOLERANCE;
    }

    public double getAngleInDegrees() {
        return m_angleInDegrees;
    }

    public int getCurrentPosition() {
        return (int) m_currentPosition;
    }

    public void initDefaultCommand() {
    }

    private double positionToDegrees(int position) {
        return position / TICKS_PER_DEGREE + STARTING_POSITION_DEGREES;
    }

    private int degreesToPosition(double degrees) {
        return (int) ((degrees - STARTING_POSITION_DEGREES) * TICKS_PER_DEGREE);
    }

    // updateSensors() should be called on every main loop, whether robot is
    // disabled, autonomous, or teleop
    // Update all sensor values and compute all terms which depend solely upon
    // sensor values
    public void updateSensors() {
        m_currentPosition = motor_A.getPosition();
        m_angleInDegrees = positionToDegrees(m_currentPosition);
        m_angleInRadians = Math.toRadians(m_angleInDegrees);

        // get a range of -1 to 1 to multiply by feedforward.
        // when in horizontal forward position, value should be 1
        // when in vertical up or down position, value should be 0
        // when in horizontal backward position, value should be -1
        m_gravityCompensation = Math.cos(m_angleInRadians);

        // HORIZONTAL_HOLD_OUTPUT is the minimum power required to hold the arm up when
        // horizontal
        // this is a range of -1.0 to 1.0 (%vbus), determined empirically
        m_feedForward = m_gravityCompensation * HORIZONTAL_HOLD_OUTPUT;
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Shoulder Current Pos", getCurrentPosition());
        SmartDashboard.putNumber("Shoulder Current Degrees", m_angleInDegrees);
        SmartDashboard.putNumber("Shoulder Desired Degrees", m_desiredAngle);
        // SmartDashboard.putNumber("Shoulder Gravity Compensation",
        // m_gravityCompensation);
        // SmartDashboard.putNumber("Shoulder FeedForward", m_feedForward);
        SmartDashboard.putNumber("Shoulder Voltage A", motor_A.getOutputVoltage());
        SmartDashboard.putNumber("Shoulder Voltage B", motor_B.getOutputVoltage());
        SmartDashboard.putNumber("Shoulder Amps A", motor_A.getOutputCurrent());
        SmartDashboard.putNumber("Shoulder Amps B", motor_B.getOutputCurrent());
        SmartDashboard.putString("Shoulder State", m_state.toString());
        // SmartDashboard.putNumber("Shoulder Joystick", Robot.oi.getOperatorLeftY());
        // SmartDashboard.putNumber("Shoulder Velocity",
        // motor_A.getSelectedSensorVelocity());
        SmartDashboard.putBoolean("Shoulder Brake", brake.get());
    }

    public void setManualMode(boolean b) {
        this.m_manualMode = b;
    }

    public void update() {

        // if the operator is moving the joystick for manual control, ensure manual mode
        // and turn off the brake

        if (Robot.oi.getOperatorLeftY() != 0.0) {
            this.m_manualMode = true;
            this.brake.set(false);
        }

        if (!m_manualMode) { // auto mode...
            switch (m_state) {

            case STOPPED:
                // TODO: after we have a brake, when we are stopped, set the motor 0 power.
                // this.motor_A.set(ControlMode.PercentOutput, 0,
                // DemandType.ArbitraryFeedForward, m_feedForward);

                // for now, however, need to keep holding our position
                this.motor_A.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle),
                        DemandType.ArbitraryFeedForward, m_feedForward);

                // set the brake.
                this.brake.set(true);
                break;

            case START_MOVING:
                // release the brake
                this.brake.set(false);

                // for now, since no brake, need to keep holding our position
                this.motor_A.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle),
                        DemandType.ArbitraryFeedForward, m_feedForward);

                // start a timer
                m_startTimer = m_timer.get();
                m_state = State.WAIT_FOR_BRAKE_RELEASE;
                break;

            case WAIT_FOR_BRAKE_RELEASE:
                // when the timer expires, set the motor to the desired position.
                if (m_timer.get() - m_startTimer >= BRAKE_RELEASE_TIME_SEC) {
                    this.motor_A.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle),
                            DemandType.ArbitraryFeedForward, m_feedForward);
                    m_state = State.MOTOR_MOVING;
                }
                break;

            case MOTOR_MOVING:
                // If we are close to position...
                if (this.isAtSetpoint()) {
                    // TODO: when we have a working brake, actually want to turn off the motor
                    // this.motor_A.set(ControlMode.PercentOutput, 0,
                    // DemandType.ArbitraryFeedForward, m_feedForward);

                    // for now though, need to keep holding our position
                    this.motor_A.set(ControlMode.MotionMagic, degreesToPosition(m_desiredAngle),
                            DemandType.ArbitraryFeedForward, m_feedForward);

                    // set the brake
                    this.brake.set(true);
                    m_state = State.STOPPED;
                }
                break;
            }
        } else { // manual mode...
            // this.m_manualMode = true; // stay in manual mode

            // if the operator is commanding a move...
            if (Robot.oi.getOperatorLeftY() != 0.0) {
                // this.m_manualMode = true;
                this.brake.set(false);
                motor_A.set(ControlMode.PercentOutput, Robot.oi.getOperatorLeftY(), DemandType.ArbitraryFeedForward,
                        m_feedForward);

            } else { // in manual mode and not commanding a move.
                this.brake.set(true);

                motor_A.set(ControlMode.PercentOutput, Robot.oi.getOperatorLeftY(), DemandType.ArbitraryFeedForward,
                        m_feedForward);
            }

        }

    }
}