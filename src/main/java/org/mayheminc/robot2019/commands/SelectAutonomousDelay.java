
package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 *
 * @author Team1519
 */
public class SelectAutonomousDelay extends InstantCommand {

    private int delta;

    public SelectAutonomousDelay(int delta) {
        this.delta = delta;
    }

    @Override
    public boolean runsWhenDisabled() {
        return (true);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.autonomous.adjustDelay(delta);
    }
}
