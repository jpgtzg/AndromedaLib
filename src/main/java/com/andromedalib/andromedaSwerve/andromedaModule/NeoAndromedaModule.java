/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import com.andromedalib.motorControllers.SuperSparkMax;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.andromedalib.sensors.SuperCANCoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.andromedalib.andromedaSwerve.systems.AndromedaSwerve;
import com.andromedalib.andromedaSwerve.utils.AndromedaModuleConstants;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;

public class NeoAndromedaModule implements AndromedaModule {
    private int moduleNumber;
    private String moduleName;

    private SuperSparkMax driveMotor;
    private SuperSparkMax steeringMotor;
    private SuperCANCoder steeringEncoder;

    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private SparkMaxPIDController driveController;
    private SparkMaxPIDController turningController;

    private PIDController steeringController;

    /**
     * Creates a new NeoAndromedaModule that uses {@link SuperSparkMax} motors
     * 
     * @param moduleNumber This module's number
     * @param moduleName   This module's name
     * @param constants    IDs and offsets constants
     */
    public NeoAndromedaModule(int moduleNumber, String moduleName, AndromedaModuleConstants constants) {
        this.moduleNumber = moduleNumber;
        this.moduleName = moduleName;

        if (AndromedaSwerve.andromedaProfile.motorConfig.equals("Falcon config")) {
            DriverStation.reportError("Neo AndromedaModule " + moduleNumber
                    + " is using Falcon config. Please change your profile config selection to avoid unwanted behaviours",
                    true);
        }

        this.driveMotor = new SuperSparkMax(constants.driveMotorID, GlobalIdleMode.brake,
        AndromedaSwerve.andromedaProfile.driveMotorInvert,
                AndromedaSwerve.andromedaProfile.driveContinuousCurrentLimit);
        this.steeringMotor = new SuperSparkMax(constants.steeringMotorID, GlobalIdleMode.Coast,
        AndromedaSwerve.andromedaProfile.steeringMotorInvert,
        AndromedaSwerve.andromedaProfile.angleContinuousCurrentLimit);
        this.steeringEncoder = new SuperCANCoder(constants.absCanCoderID,
        AndromedaSwerve.andromedaProfile.cancoderConfig);
        this.angleOffset = constants.angleOffset;

        this.driveController = driveMotor.getPIDController();
        this.turningController = steeringMotor.getPIDController();

        steeringController = new PIDController(AndromedaSwerve.andromedaProfile.turningKp,
        AndromedaSwerve.andromedaProfile.turningKi, AndromedaSwerve.andromedaProfile.turningKf);

        steeringController.enableContinuousInput(-180, 180);

        turningController.setP(AndromedaSwerve.andromedaProfile.turningKp);
        turningController.setI(AndromedaSwerve.andromedaProfile.turningKi);
        turningController.setD(AndromedaSwerve.andromedaProfile.turningKi);

        driveController.setP(AndromedaSwerve.andromedaProfile.driveKp);
        driveController.setI(AndromedaSwerve.andromedaProfile.driveKi);
        driveController.setD(AndromedaSwerve.andromedaProfile.driveKd);

        resetAbsolutePosition();

        driveMotor.setPositionConversionFactor(AndromedaSwerve.andromedaProfile.driveGearRatio);
        driveMotor.setVelocityConversionFactor(AndromedaSwerve.andromedaProfile.driveGearRatio / 60);
        steeringMotor.setPositionConversionFactor(AndromedaSwerve.andromedaProfile.steeringGearRatio);
        steeringMotor.setVelocityConversionFactor(AndromedaSwerve.andromedaProfile.steeringGearRatio / 60);

        lastAngle = getAngle();

    }

    @Override
    public int getModuleNumber() {
        return moduleNumber;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
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
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConstants.maxSpeed * 0.01))
                ? lastAngle
                : desiredState.angle;

        setSparkAngle(angle.getDegrees());

        lastAngle = angle;
    }

    /**
     * Sets the drive motor speed to its desired state
     * 
     * @param desiredState {@link SwerveModuleState} to apply
     * @param isOpenLoop   True if open loop feedback is enabled
     */
    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
        if (isOpenLoop) {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConstants.maxSpeed;

            driveMotor.set(percentOutput);
        } else {
            driveController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity);
        }
    }

    @Override
    public void resetAbsolutePosition() {
        steeringMotor.setPosition(0);
        double encoderPosition = steeringEncoder.getAbsolutePosition() - angleOffset.getDegrees();
        steeringMotor.setPosition(encoderPosition);
    }

    /**
     * Gets the current module angle
     * 
     * @return Current {@link Rotation2D}
     */
    private Rotation2d getAngle() {
        return Rotation2d.fromDegrees(
                steeringMotor.getPosition());
    }

    @Override
    public SwerveModuleState getState() {
        return new SwerveModuleState(driveMotor.getVelocity(), getAngle());
    }

    @Override
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                driveMotor.getPosition(),
                getAngle());
    }

    /**
     * Sets the sparkmax angle and optimizes the angle to use
     * {@link SparkMaxPIDController} loop
     * 
     * @param targetAngleInDegrees target angle from WPI's swerve kinematics
     *                             optimize method
     */
    private void setSparkAngle(double targetAngleInDegrees) {

        double currentSparkAngle = getAngle().getDegrees();

        double sparkRelativeTargetAngle = reboundValue(targetAngleInDegrees, currentSparkAngle);

        turningController.setReference(sparkRelativeTargetAngle,
                ControlType.kPosition);
    }

    /**
     * Calculates the correct angle and optimizes
     * 
     * @param value  Target Angle
     * @param anchor Current angle
     * @return Appropiate relativeAngle
     */
    private double reboundValue(double value, double anchor) {
        double lowerBound = anchor - 180;
        double upperBound = anchor + 180;

        if (value < lowerBound) {
            value = upperBound
                    + ((value - lowerBound) % (upperBound - lowerBound));
        } else if (value > upperBound) {
            value = lowerBound
                    + ((value - upperBound) % (upperBound - lowerBound));
        }

        return value;
    }

    /* Telemetry */

    @Override
    public double[] getTemp() {
        return new double[] { steeringMotor.getMotorTemperature(), driveMotor.getMotorTemperature() };
    }
}
