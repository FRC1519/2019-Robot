/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.mayheminc.robot2019.subsystems.*;

/**
 *
 */
public class AutoClimb extends CommandGroup {

    /**
     * Full-power to linkage until approximently vertical Start driving robot wheels
     * forward at 1/4 speed for about 5 seconds Continue moving linkage until just
     * forward at 1/4 speedfor about 6 seconds Stop driving forward
     */
    public AutoClimb() {
        // first, extend the lifter pistons, and wait for them to be extended before
        // deploying the 4-bar linkage
        addSequential(new LiftCylindersUntilDeployed());
        addSequential(new Wait(0.0));

        // second, deploy the 4-bar linkage, starting the drive motors a half second
        // later
        addParallel(new LifterLift(Lifter.LIFTED_POS));

        addSequential(new Wait(0.5)); // wait a half-second before starting to drive
        addSequential(new DriveStraightForTime(.60, 2.5));

        // hopefully, by now we will have actually climbed, so retract the cylinders
        addSequential(new LiftCylindersSetAndStay(LiftCylinders.RETRACTED));
    }
}