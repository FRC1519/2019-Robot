package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.robot2019.RobotMap;

public class Lifter extends Subsystem {

    public static final int STARTING_POS = 0;
    public static final int LIFTED_POS = 10000; // dummy value
    public static final double TUCKED_POWER = 0.10;

    private final MayhemTalonSRX motorLeft = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRight = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    public Lifter() {

        // configure the Left Motor
        motorLeft.setNeutralMode(NeutralMode.Coast);
        motorLeft.configNominalOutputVoltage(+0.0f, -0.0f);
        motorLeft.configPeakOutputVoltage(+12.0, -6.0);
        motorLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);

        // the Right motor follows the left.
        motorRight.changeControlMode(ControlMode.Follower);
        motorRight.set(motorLeft.getDeviceID());

        Tuck();
    }

    public void Lift() {
        motorLeft.set(ControlMode.Position, Lifter.LIFTED_POS);
    }

    public void Tuck() {
        // start with percent output to keep the lifter tucked under the belly.
        motorLeft.set(ControlMode.PercentOutput, TUCKED_POWER);
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Lifter Pos", motorLeft.getSelectedSensorPosition());
    }

}