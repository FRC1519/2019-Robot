
package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 *
 * @author Team1519
 */
public class SelectAutonomousProgram extends InstantCommand {

    private int m_delta;

    public SelectAutonomousProgram(int delta) {
        m_delta = delta;
    }

    @Override
    public boolean runsWhenDisabled() {
        return (true);
    }

    // Called just before this Command runs the first time
    public void initialize() {
        Robot.autonomous.adjustProgramNumber(m_delta);
    }
}
