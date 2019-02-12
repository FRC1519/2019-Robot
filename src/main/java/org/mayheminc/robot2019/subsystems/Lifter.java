package org.mayheminc.robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.mayheminc.util.MayhemTalonSRX;
import org.mayheminc.robot2019.RobotMap;

public class Lifter extends Subsystem {

    private static final int STARTING_POS = 0;
    private static final int LIFTED_POS = 10000; // dummy value
    private static final double STOP_POWER = 0.10;
    private static final double TUCKED_POWER = 0.10;
    private static final int IN_POSITION_SLOP = 100;
    private static final int TicksToClimb = 320000;
    private static final double maxPower = 0.5;
    private static final int MAX_MOTOR_OFFSET = 5000;    
    private static final double CHANGE_POWER = 0.4;

    private final MayhemTalonSRX motorLeft = new MayhemTalonSRX(RobotMap.LIFTER_LEFT_A_TALON);
    private final MayhemTalonSRX motorRight = new MayhemTalonSRX(RobotMap.LIFTER_RIGHT_A_TALON);

    private int past_pos_r = 0;
    private int past_pos_l = 0;

    private int m_pos;

    private Boolean StartClimb = false;



    public Lifter() {

        //Config right motor
        ConfigMotor(motorLeft);
        ConfigMotor(motorRight);
        //Config 
        // the Right motor follows the left.
        // motorRight.changeControlMode(ControlMode.Follower);
        // motorRight.set(motorLeft.getDeviceID());

        Tuck();
    }

    public void ConfigMotor(MayhemTalonSRX motor){
        motor.setNeutralMode(NeutralMode.Coast);
        motor.configNominalOutputVoltage(+0.0f, -0.0f);
        motor.configPeakOutputVoltage(+12.0, -6.0);
        motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    }

    public void AutoLift(){
        //Get the encoder postions
        if(this.StartClimb){
            int pos_r = motorRight.getSelectedSensorPosition();
            int pos_l = motorLeft.getSelectedSensorPosition();
    
            //Find out if the difference is too great
            if (Math.abs(pos_r-pos_l) < MAX_MOTOR_OFFSET){
                motorRight.set(ControlMode.PercentOutput, maxPower);
                motorLeft.set(ControlMode.PercentOutput, maxPower);
            }
            //Change based on the offset
            else if(pos_r > pos_l){
                motorRight.set(ControlMode.PercentOutput, CHANGE_POWER);
            }
            else if(pos_r < pos_l){
                motorLeft.set(ControlMode.PercentOutput, CHANGE_POWER);
            }          
            //Stop if done climbing
            if(pos_r >= TicksToClimb){
                motorRight.set(ControlMode.PercentOutput, STOP_POWER);
                motorLeft.set(ControlMode.PercentOutput, STOP_POWER);
                this.StartClimb = false;
            }
        }
    }

    public void Lift() {
        //Tell autolift to climb
        motorRight.set(ControlMode.PercentOutput, maxPower);
        motorLeft.set(ControlMode.PercentOutput, maxPower);
        // this.StartClimb = false;
    }

    public void Tuck() {
        // start with percent output to keep the lifter tucked under the belly.
        motorLeft.set(ControlMode.PercentOutput, TUCKED_POWER);
        motorRight.set(ControlMode.PercentOutput, TUCKED_POWER);
        m_pos = Lifter.STARTING_POS;
    }

    public void Stop() {
        motorLeft.set(ControlMode.PercentOutput, STOP_POWER);
        motorRight.set(ControlMode.PercentOutput, STOP_POWER);
    }

    public boolean IsAtSetpoint() {
        return Math.abs(motorLeft.getPosition() - m_pos) < Lifter.IN_POSITION_SLOP;
    }

    public void initDefaultCommand() {
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Lifter Pos R", motorRight.getSelectedSensorPosition());
        SmartDashboard.putNumber("Lifter Pos L", motorLeft.getSelectedSensorPosition());
  
    }

}