package com.andromedalib.andromedaSwerve.andromedaModule;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/**
 * AndromedaModule interface. All available modules inherit this interface
 */
public interface AndromedaModule {
    public int getModuleNumber();

    public String getModuleName();

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop);

    public void resetAbsolutePosition();

    public SwerveModuleState getState();

    public SwerveModulePosition getPosition();

    public double[] getTemp();

}
