package org.mayheminc.robot2019.subsystems;

import org.mayheminc.robot2019.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shifter extends Subsystem {
	// Solenoid
	Solenoid m_shifter;

	public Shifter()
	{
		m_shifter = new Solenoid(RobotMap.SHIFTING_SOLENOID);
	}
	
	public void initDefaultCommand() {}
	
	//**********************************SHIFTER PISTONS***********************************************

	public static final boolean HIGH_GEAR = true;
	public static final boolean LOW_GEAR = !HIGH_GEAR;
	private boolean m_highGear = HIGH_GEAR; // flag for current gear setting

	public final void setGear(boolean gear) {
		m_highGear = gear;
		if (m_highGear == HIGH_GEAR) {
			m_shifter.set(HIGH_GEAR);  
		} else {
			m_shifter.set(LOW_GEAR);
		}
	}

	public boolean getGear() {
		return m_highGear;
	}

	public void updateSmartDashboard() {
		SmartDashboard.putBoolean("High Gear", !m_highGear);	
	}
}
