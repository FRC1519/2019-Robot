package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class PrintAutonomousTimeRemaining extends CommandBase {
	String Mesage = ""; 
    public PrintAutonomousTimeRemaining(String msg) {
        this.Mesage = msg;
    }
    
    // Called just before this Command runs the first time
    @Override
    public void initialize() {
		DriverStation.reportError(Mesage + " At: " + Robot.autonomousTimeRemaining() + "\n", false);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }
}
