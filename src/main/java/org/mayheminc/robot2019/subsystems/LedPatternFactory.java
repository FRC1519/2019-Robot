/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import java.util.ArrayList;

import org.mayheminc.robot2019.subsystems.LEDLights.PatternID;

/**
 * Add your docs here.
 */
public class LedPatternFactory {

    public static CyclePattern defaultAuto = new CyclePattern() {
        {
            add(new CycleElement(PatternID.BLUE, 0.5));
            add(new CycleElement(PatternID.BLACK, 0.5));
        }
    };

    public static CyclePattern defaultTeleOp = new CyclePattern() {
        {
            add(new CycleElement(PatternID.RED, 0.5));
            add(new CycleElement(PatternID.BLACK, 0.5));
        }
    };

    public static CyclePattern cargoBall = new CyclePattern() {
        {
            add(new CycleElement(PatternID.ORANGE, 0.4));
            add(new CycleElement(PatternID.WHITE, 0.2));
            this.m_timeLength = 2.0;
        }
    };

    public static CyclePattern hatchPanel = new CyclePattern() {
        {
            add(new CycleElement(PatternID.YELLOW, 0.125));
            add(new CycleElement(PatternID.BLACK, 0.125));
            this.m_timeLength = 1.0;
        }
    };

    public static CyclePattern test1 = new CyclePattern() {
        {
            add(new CycleElement(PatternID.RED, 1.0));
            add(new CycleElement(PatternID.GREEN, 1.0));
            add(new CycleElement(PatternID.BLUE, 1.0));
            this.m_timeLength = 10.0;
        }
    };
}