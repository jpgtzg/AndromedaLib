/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/**
 * AndromedaModule interface. All available modules inherit this interface
 */
public interface AndromedaModule {
    /**
     * Gets the module's number
     * 
     * @return Module number
     */
    public int getModuleNumber();

    /**
     * Gets the module's name
     * 
     * @return Module name
     */
    public String getModuleName();

    /**
     * Sets the Module's state
     * 
     * @param desiredState Desired {@link SwerveModuleState} to apply
     * @param isOpenLoop   True if open loop feedback is enabled
     */
    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop);

    /**
     * Resets the module's angle to the absolute encoder position
     */
    public void resetAbsolutePosition();

    /**
     * Gets the current module state
     * 
     * @return Current {@link SwerveModuleState}
     */
    public SwerveModuleState getState();

    /**
     * Gets the current desired module state
     * 
     * @return Current desired {@link SwerveModuleState}
     */
    public SwerveModuleState getDesiredState();

    /**
     * Gets the current module state in a double array
     * 
     * @return Current {@link SwerveModuleState} in a double array
     */
    public double[] getDoubleStates();

    /**
     * Gets the current desired module state in a double array
     * 
     * @return Current desired {@link SwerveModuleState} in a double array
     */
    public double[] getDoubleDesiredStates();

    /**
     * Gets the current module position
     * 
     * @return Current {@link SwerveModulePosition} position
     */
    public SwerveModulePosition getPosition();

    /**
     * Gets module's motor temps
     * 
     * @return
     */
    public double[] getTemp();

    /**
     * Updates all NT values
     */
    public void updateNT();

}
