package org.mayheminc.robot2019.commands;
import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShifterHoldGear extends Command {
    boolean m_gear;
    
    public ShifterHoldGear(boolean gear) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
//    	requires(Robot.drive);
    	m_gear = gear;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.shifter.setGear(m_gear);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shifter.setGear(!m_gear);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.shifter.setGear(!m_gear);
    }
}
