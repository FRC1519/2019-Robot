package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 *
 */
public class Wait extends CommandBase {
    Timer m_Timer = new Timer();
    double m_endTime;

    public Wait() {
        this(0);
    }

    public Wait(double endTime) {
        // set the name of this object to include the duration of the wait
        setName("Wait " + endTime + " seconds");
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_endTime = endTime;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        m_Timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return m_Timer.hasPeriodPassed(m_endTime);
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
    }
}
