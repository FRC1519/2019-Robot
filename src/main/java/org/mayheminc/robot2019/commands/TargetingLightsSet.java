package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TargetingLightsSet extends Command {
    private boolean m_onOrOff;

    public TargetingLightsSet(boolean arg_onOrOff) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_onOrOff = arg_onOrOff;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.targetingLights.set(m_onOrOff);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.targetingLights.set(m_onOrOff);
    }
}
