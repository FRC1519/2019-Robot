package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class TurnOnCompressor extends InstantCommand {

    public TurnOnCompressor() {
    	super();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.compressor.start();
    }
}
