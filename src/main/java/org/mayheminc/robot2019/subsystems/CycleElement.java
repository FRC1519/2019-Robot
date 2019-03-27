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
    private double m_phaseTime;
    private double m_durationTimeout;

    public CycleElement(PatternID patternID, double phaseTime, double durationTimeout) {
        m_patternID = patternID;
        m_phaseTime = phaseTime;
        m_durationTimeout = durationTimeout;
    }

    // only needed methods are a get and set for each instance variable.
    public double getPhaseTime() {
        return m_phaseTime;
    }

    public double getDurationTimeout() {
        return m_durationTimeout;
    }

    public PatternID getPatternID() {
        return m_patternID;
    }

    public void setPhaseTime(double phaseTime) {
        m_phaseTime = phaseTime;
    }

    public void setDurationTimeout(double durationTimeout) {
        m_durationTimeout = durationTimeout;
    }

    public void setPatternID(PatternID patternID) {
        m_patternID = patternID;
    }
}
