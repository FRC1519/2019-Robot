package org.mayheminc.robot2019.commands;
import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class ShifterHoldGear extends CommandBase {
    boolean m_gear;
    
    public ShifterHoldGear(boolean gear) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.drive);
    	m_gear = gear;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.shifter.setGear(m_gear);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true or the command is interrupted
    @Override 
    public void end(boolean interrupted) {
    	Robot.shifter.setGear(!m_gear);
    }
}
