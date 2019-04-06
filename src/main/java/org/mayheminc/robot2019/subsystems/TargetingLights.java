/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class TargetingLights extends Subsystem {

    Solenoid lightPower = new Solenoid(RobotMap.TARGETING_LIGHTS_SOLENOID);
    double m_offStartTimer;

    public static final boolean LIGHTS_ON = true;
    public static final boolean LIGHTS_OFF = false;

    private final double TARGET_LIGHTS_OFF_TIME = 2.0;

    @Override
    protected void initDefaultCommand() {
    }

    public void set(boolean b) {
        lightPower.set(b);

        // if we turn off the lights, start a timer.
        if (!b) {
            m_offStartTimer = Timer.getFPGATimestamp();
        }
    }

    public void update() {
        // if the timer is done, turn on the lights.
        if (Timer.getFPGATimestamp() - m_offStartTimer >= TARGET_LIGHTS_OFF_TIME) {
            set(true);
        }
    }

}
