/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mayheminc.robot2019.autonomousroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.mayheminc.robot2019.commands.*;

/**
 *
 * @author Team1519
 */
public class StayStill extends CommandGroup {
    
    public StayStill() {
        
        // Perform needed initialization
        addSequential(new DriveZeroGyro());

       // ALL DONE!
    }
}
