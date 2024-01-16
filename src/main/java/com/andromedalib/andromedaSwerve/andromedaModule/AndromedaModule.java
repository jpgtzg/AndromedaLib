/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import org.littletonrobotics.junction.Logger;

import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig.Mode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class AndromedaModule {
    private final int moduleNumber;
    private final String moduleName;

    private AndromedaModuleIO io;
    private AndromedaModuleIOInputsAutoLogged inputs = new AndromedaModuleIOInputsAutoLogged();

    private Rotation2d lastAngle;
    private AndromedaSwerveConfig andromedaSwerveConfig;

    private final SimpleMotorFeedforward driveFeedforward;
    private final PIDController driveFeedback;
    private final PIDController turnFeedback;

    public AndromedaModule(int moduleNumber, String name,
            AndromedaSwerveConfig swerveConfig, Mode mode, AndromedaModuleIO io) {
        this.io = io;
        this.moduleName = name;
        this.moduleNumber = moduleNumber;
        this.andromedaSwerveConfig = swerveConfig;

        /* Remove unused warning */
        moduleName.getClass();

        lastAngle = getAngle();

        switch (mode) {
            case REAL:
            case REPLAY:
                driveFeedforward = new SimpleMotorFeedforward(0.1, 0.13);
                driveFeedback = new PIDController(0.05, 0.0, 0.0);
                turnFeedback = new PIDController(0.3, 0.0, 0.0);
                break;
            case SIM:
                driveFeedforward = new SimpleMotorFeedforward(0.0, 0.13);
                driveFeedback = new PIDController(0.1, 0.0, 0.0);
                turnFeedback = new PIDController(10.0, 0.0, 0.0);
                break;
            default:
                driveFeedforward = new SimpleMotorFeedforward(0.0, 0.0);
                driveFeedback = new PIDController(0.0, 0.0, 0.0);
                turnFeedback = new PIDController(0.0, 0.0, 0.0);
                break;
        }

        turnFeedback.enableContinuousInput(-Math.PI, Math.PI);

    }

    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Swerve/Module" + Integer.toString(moduleNumber), inputs);

    }

    public void setDesiredState(SwerveModuleState desiredState) {
        desiredState = SwerveModuleState.optimize(desiredState, getAngle());

        setAngle(desiredState);
        setSpeed(desiredState);
    }

    /**
     * Sets the turning motor´+´+ angle to its desired state
     * 
     * @param desiredState {@link SwerveModuleState} to apply
     */
    private void setAngle(SwerveModuleState desiredState) {
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (andromedaSwerveConfig.maxSpeed * 0.01))
                ? lastAngle
                : desiredState.angle;

        io.setTurnVoltage(turnFeedback.calculate(getAngle().getRadians(), angle.getRadians()));
        lastAngle = angle;
    }

    /**
     * Sets the drive motor speed to its desired state
     * 
     * @param desiredState {@link SwerveModuleState} to apply
     * @param isOpenLoop   True if open loop feedback is enabled
     */
    private void setSpeed(SwerveModuleState desiredState) {
        io.setDriveVoltage(
                driveFeedforward.calculate(desiredState.speedMetersPerSecond)
                        + driveFeedback.calculate(inputs.driveVelocity, desiredState.speedMetersPerSecond));
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
        return inputs.steerAngle;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(inputs.drivePosition, getAngle());
    }
}
