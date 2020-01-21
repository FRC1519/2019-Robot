
package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 * @author Team1519
 */
public class Delay extends CommandBase {
    private long m_delay;
    private long m_endTime;

    public Delay(long delay) {
        setName("Delay");
        m_delay = delay;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        m_endTime = System.currentTimeMillis() + m_delay;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() >= m_endTime;
    }
}
