/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import org.littletonrobotics.junction.Logger;

import com.andromedalib.andromedaSwerve.config.AndromedaModuleConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class AndromedaModule {
    private final int moduleNumber;
    private final String moduleName;

    private AndromedaModuleIO io;
    private AndromedaModuleIOInputsAutoLogged inputs = new AndromedaModuleIOInputsAutoLogged();

    private Rotation2d lastAngle;
    private AndromedaModuleConfig andromedaModuleConfig;
    private AndromedaSwerveConfig andromedaSwerveConfig;

    public AndromedaModule(int moduleNumber, String name, AndromedaModuleConfig config,
            AndromedaSwerveConfig swerveConfig, AndromedaModuleIO io) {
        this.io = io;
        this.moduleName = name;
        this.moduleNumber = moduleNumber;
        this.andromedaModuleConfig = config;
        this.andromedaSwerveConfig = swerveConfig;
        
        /* Remove unused warning */
        moduleName.getClass();
        andromedaModuleConfig.getClass();
        
        lastAngle = getAngle();
    }

    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Swerve/Module" + Integer.toString(moduleNumber), inputs);

    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
        desiredState = SwerveModuleState.optimize(desiredState, getState().angle);

        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    /**
     * Sets the turning motor angle to its desired state
     * 
     * @param desiredState {@link SwerveModuleState} to apply
     */
    private void setAngle(SwerveModuleState desiredState) {
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (andromedaSwerveConfig.maxSpeed * 0.01))
                ? lastAngle
                : desiredState.angle;

        io.setTurnPosition(angle);
        lastAngle = angle;
    }

    /**
     * Sets the drive motor speed to its desired state
     * 
     * @param desiredState {@link SwerveModuleState} to apply
     * @param isOpenLoop   True if open loop feedback is enabled
     */
    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {

        io.setDriveSpeed(desiredState.speedMetersPerSecond, isOpenLoop);

        // TODO REMOVE
       /*  if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / andromedaSwerveConfig.maxSpeed;

            io.setDrivePercent(percentOutput);
        } else {
            double velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond,
                    this.andromedaModuleConfig.wheelCircumference);
            double feedforward = motorFeedforward.calculate(desiredState.speedMetersPerSecond);
            
            io.setDriveVelocity(feedforward);
        } */
    }

    /**
     * Gets the current position and angle as a {@link SwerveModuleState}
     * 
     * @return
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(getDriveSpeed(), getAngle());
    }

    public double getDriveSpeed() {
        return inputs.driveVelocity;
    }

    private Rotation2d getAngle() {
        return Rotation2d.fromDegrees(inputs.steerAngle);
    }
}
