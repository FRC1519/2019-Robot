/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.mayheminc.robot2019.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import org.mayheminc.robot2019.commands.DriveStraight.DistanceUnits;
import org.mayheminc.robot2019.subsystems.*;

/**
 *
 */
public class AutoClimbL2 extends SequentialCommandGroup {

    /**
     * L2 climb strategy: 0 - Start with robot forwards against L2 1 - Power lifter
     * to raise front of the robot above L2 2 - Drive forward a few inches to have
     * lifter pressed against L2 wall 3 - Retract lifter fully into robot 4 - Drive
     * a little further forward 5 - Extend Lifter pistons to raise rear of robot 6 -
     * Gently drive robot forward until 6 wheels on L2 7 - Raise pistons 8 - Drive
     * fully onto platform
     * 
     */
    public AutoClimbL2() {

        // * 0 - Start with robot forwards against L2

        addCommands(
                // * 1 - Power lifter to raise front of the robot above L2
                new LifterSetPosition(300000),

                // * 2 - Drive forward a few inches to have lifter pressed against L2 wall
                new DriveStraightForTime(0.50, 0.50), // drive at 50% power, 3/4 second

                // * 3 - Retract lifter fully into robot, while gently holding the robot against
                // the L2 platform so it doesn't roll back off
                new ParallelDeadlineGroup(new LifterSetPosition(0), new DriveStraightForTime(0.10, 1.0)), // drive for
                                                                                                          // up to one
                                                                                                          // second, but
                                                                                                          // will
                                                                                                          // terminate
                                                                                                          // early due
                                                                                                          // to deadline

                // * 4 - Drive a little further forward
                new DriveStraightForTime(0.50, 1.0),

                // * 5 - Extend Lifter pistons to raise rear of robot and simultaneously turn
                // off the lifter again
                new ParallelCommandGroup(new LiftCylindersSetAndStay(LiftCylinders.EXTENDED), new LifterSetManual(0.0)), // turn
                                                                                                                         // the
                                                                                                                         // lifter
                                                                                                                         // off
                                                                                                                         // again

                new Wait(1.0), // give pistons one second to move

                // * 6 - Gently drive robot forward until 6 wheels on L2
                new DriveStraight(0.35, DistanceUnits.INCHES, 12.0),

                // * 7 - Raise pistons
                new LiftCylindersSetAndStay(LiftCylinders.RETRACTED),

                // wait for cylinders to retract before starting to drive again
                new Wait(1.0), // give pistons one second to move

                // * 8 - Drive fully onto platform (one foot more)
                new DriveStraight(0.20, DistanceUnits.INCHES, 12.0),

                // end of program; wait so the program doesn't just restart again!
                new WaitForever());
    }
}