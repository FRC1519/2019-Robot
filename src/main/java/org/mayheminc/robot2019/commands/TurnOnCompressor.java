package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 *
 */
public class TurnOnCompressor extends InstantCommand {

    public TurnOnCompressor() {
    	super();
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    	Robot.compressor.start();
    }
}
