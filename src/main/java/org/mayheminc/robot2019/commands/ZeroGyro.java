package org.mayheminc.robot2019.commands;

import org.mayheminc.robot2019.Robot;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * DOES NOT fully calibrate the navx this command will simply reset the navx
 */
public class ZeroGyro extends InstantCommand {

    private double m_headingOffset = 0.0;

    public ZeroGyro() {
        this(0.0);
    }

    public ZeroGyro(double headingOffset) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_headingOffset = headingOffset;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        Robot.drive.zeroHeadingGyro(m_headingOffset);
    }
}
