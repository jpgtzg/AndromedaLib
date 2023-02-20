/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.motorControllers;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

/**
 * Global class to manage the idle mode of all motors
 * This is to make it easier to manage motor states
 */
public class IdleManager {

    public enum GlobalIdleMode {
        Coast, brake
    }

    /**
     * Converts from GlobalIdle mode to corresponding NeutralMode
     * 
     * @param idleMode The GlobalIdleMode to convert
     * 
     * @return The corresponding NeutralMode
     */
    static NeutralMode idleToNeutral(GlobalIdleMode idleMode) {
        return NeutralMode.values()[idleMode.ordinal() + 1];
    }

    /**
     * Converts from GlobalIdle mode to corresponding IdleMode
     * 
     * @param idleMode The GlobalIdleMode to convert
     * 
     * @return The corresponding IdleMode
     */
    static IdleMode neutralToIdle(GlobalIdleMode idleMode) {
        return IdleMode.values()[idleMode.ordinal()];
    }

}
