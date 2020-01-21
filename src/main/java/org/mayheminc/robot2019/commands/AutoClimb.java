/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.mayheminc.robot2019.subsystems.*;

/**
 *
 */
public class AutoClimb extends SequentialCommandGroup {

    /**
     * Full-power to linkage until approximently vertical Start driving robot wheels
     * forward at 1/4 speed for about 5 seconds Continue moving linkage until just
     * forward at 1/4 speedfor about 6 seconds Stop driving forward
     */
    public AutoClimb() {
        addCommands(
                // first, extend the lifter pistons, and wait for them to be extended
                new LiftCylindersUntilDeployed(),

                // next, commence two parallel activities, each of which is a separate sequence
                // of actions:
                new ParallelCommandGroup(
                        // A - deploy the lifter and then raise the cylinders
                        new SequentialCommandGroup(new LifterLift(Lifter.LIFTED_POS),
                                new LiftCylindersSetAndStay(LiftCylinders.RETRACTED)),
                        // B - after a half-second delay, start driving the robot at a little more than
                        // half-speed for 2.5 seconds
                        new SequentialCommandGroup(new Wait(0.5), // wait a half-second before starting to drive
                                new DriveStraightForTime(.60, 2.5))));
    }
}