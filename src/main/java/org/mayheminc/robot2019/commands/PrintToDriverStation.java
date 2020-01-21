package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class PrintToDriverStation extends CommandBase {

	String m_str;
    public PrintToDriverStation(String str) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_str = str;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    	DriverStation.reportError(m_str,  false);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }
}
