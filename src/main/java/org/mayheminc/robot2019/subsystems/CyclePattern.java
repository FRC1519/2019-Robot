/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.subsystems;

import java.util.ArrayList;

/**
 * A CyclePattern is a list of Cycle Elements with a timeout.
 */
public class CyclePattern {
    private ArrayList<CycleElement> m_List;
    double m_timeLength;

    public void setTimeout(double d) {
        m_timeLength = d;
    }

    public double getTimeout() {
        return m_timeLength;
    }

    public void add(CycleElement e) {
        m_List.add(e);
        m_timeLength += e.getDurationTimeout();
    }

    public CycleElement peek() {
        return m_List.get(0);
    }

    public void Rotate() {
        CycleElement ele = m_List.get(0);
        m_List.remove(0);
        m_List.add(ele);
    }
}