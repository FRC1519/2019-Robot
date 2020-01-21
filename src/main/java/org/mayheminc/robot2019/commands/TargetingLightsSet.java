package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class TargetingLightsSet extends CommandBase {
    private boolean m_onOrOff;

    public TargetingLightsSet(boolean arg_onOrOff) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_onOrOff = arg_onOrOff;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        Robot.targetingLights.set(m_onOrOff);
    }
}
