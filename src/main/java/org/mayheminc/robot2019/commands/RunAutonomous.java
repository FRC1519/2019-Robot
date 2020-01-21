
package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 * @author Team1519
 */
public class RunAutonomous extends CommandBase {
    private long startTime;
    private Command command;

    public RunAutonomous() {
    }

    // Called just before this Command runs the first time
    public void initialize() {
        startTime = System.currentTimeMillis() + Robot.autonomous.getDelay() * 1000;
        command = Robot.autonomous.getSelectedProgram();
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
        if (System.currentTimeMillis() >= startTime) {
            command.schedule();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    public boolean isFinished() {
        return System.currentTimeMillis() >= startTime;
    }

    // Called once after isFinished returns true
    public void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void interrupted() {
    }
}
