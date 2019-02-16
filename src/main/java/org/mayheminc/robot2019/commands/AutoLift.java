/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.mayheminc.robot2019.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2019.subsystems.*;

/**
 *
 */
public class AutoLift extends CommandGroup {

    /**
     * Full-power to linkage until approximently vertical Start driving robot wheels
     * forward at 1/4 speed for about 5 seconds Continue moving linkage until just
     * forward at 1/4 speedfor about 6 seconds Stop driving forward
     */
    public AutoLift() {
        addSequential(new LifterToPosition(Lifter.AUTO_LIFTED_POS_1)); // 100k encoder counts
        addParallel(new DriveStraightForTime(.25, 5.0));
        addSequential(new LifterToPosition(Lifter.AUTO_LIFTED_POS_2)); // 200k encoder counts
        addSequential(new DriveStraight(.25, DistanceUnits.INCHES, 6.0));
    }
}