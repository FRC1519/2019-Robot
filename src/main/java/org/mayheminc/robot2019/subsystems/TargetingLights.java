/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class TargetingLights extends Subsystem {

    Solenoid lightPower = new Solenoid(RobotMap.TARGETING_LIGHTS_SOLENOID);

    @Override
    protected void initDefaultCommand() {
    }

    public void set(boolean b) {
        lightPower.set(b);
    }

}
