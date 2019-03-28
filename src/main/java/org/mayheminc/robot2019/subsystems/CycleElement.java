/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.subsystems.LEDLights.PatternID;

public class CycleElement {

    private PatternID m_patternID;
    private double m_durationTimeout;

    public CycleElement(PatternID patternID, double durationTimeout) {
        m_patternID = patternID;
        m_durationTimeout = durationTimeout;
    }

    public double getDurationTimeout() {
        return m_durationTimeout;
    }

    public PatternID getPatternID() {
        return m_patternID;
    }
}
