/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import com.andromedalib.andromedaSwerve.config.AndromedaModuleConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaModuleConfig.ModuleMotorConfig;
import com.andromedalib.andromedaSwerve.subsystems.AndromedaSwerve;
import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.andromedalib.motorControllers.SuperTalonFX;
import com.andromedalib.sensors.SuperCANCoder;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.google.flatbuffers.Constants;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * AndromedaModule IO Implementation for TalonFX
 */
public class AndromedaModuleIOTalonFX implements AndromedaModuleIO {
    private SuperTalonFX driveMotor;
    private SuperTalonFX steeringMotor;
    private SuperCANCoder steeringEncoder;

    private PositionVoltage angleSetter = new PositionVoltage(0).withSlot(0);
    private VelocityVoltage driveSetter = new VelocityVoltage(0).withSlot(0);
    private final DutyCycleOut driveDutyCycle = new DutyCycleOut(0);

    private AndromedaSwerveConfig andromedaSwerveConfig;
    private AndromedaModuleConfig andromedaModuleConfig;

    private final SimpleMotorFeedforward feedforward;

    private final StatusSignal<Double> drivePosition;
    private final StatusSignal<Double> driveVelocity;
    private final StatusSignal<Double> driveAppliedVolts;
    private final StatusSignal<Double> driveCurrent;

    private final StatusSignal<Double> turnAbsolutePosition;
    private final StatusSignal<Double> turnPosition;
    private final StatusSignal<Double> turnVelocity;
    private final StatusSignal<Double> turnAppliedVolts;
    private final StatusSignal<Double> turnCurrent;

    public AndromedaModuleIOTalonFX(int moduleNumber, AndromedaModuleConfig moduleConfig,
            AndromedaSwerveConfig swerveConfig) {
        this.andromedaModuleConfig = moduleConfig;
        this.andromedaSwerveConfig = swerveConfig;

        if (andromedaModuleConfig.motorConfig == ModuleMotorConfig.SPARKMAX_CONFIG) {
            DriverStation.reportError("AndromedaModule " + moduleNumber
                    + " is using Neo config. Please change your profile config selection to avoid unwanted behaviours",
                    true);
        }

        this.driveMotor = new SuperTalonFX(andromedaModuleConfig.moduleIDs.driveMotorID, GlobalIdleMode.brake,
                andromedaModuleConfig.driveMotorConfiguration,
                andromedaModuleConfig.swerveCANBus);
        this.steeringMotor = new SuperTalonFX(andromedaModuleConfig.moduleIDs.steeringMotorID, GlobalIdleMode.Coast,
                andromedaModuleConfig.turningMotorConfiguration,
                andromedaModuleConfig.swerveCANBus);

        this.steeringEncoder = new SuperCANCoder(andromedaModuleConfig.moduleIDs.absCanCoderID,
                andromedaModuleConfig.cancoderConfiguration,
                andromedaModuleConfig.swerveCANBus);

        feedforward = new SimpleMotorFeedforward(andromedaModuleConfig.driveMotorConfiguration.Slot0.kS,
                andromedaModuleConfig.driveMotorConfiguration.Slot0.kA,
                andromedaModuleConfig.driveMotorConfiguration.Slot0.kV);

        resetAbsolutePosition();

        drivePosition = driveMotor.getPosition();
        driveVelocity = driveMotor.getVelocity();
        driveAppliedVolts = driveMotor.getMotorVoltage();
        driveCurrent = driveMotor.getStatorCurrent();

        turnAbsolutePosition = steeringEncoder.getAbsolutePosition();
        turnPosition = steeringMotor.getPosition();
        turnVelocity = steeringMotor.getVelocity();
        turnAppliedVolts = steeringMotor.getMotorVoltage();
        turnCurrent = steeringMotor.getStatorCurrent();

        BaseStatusSignal.setUpdateFrequencyForAll(
                100.0, drivePosition, turnPosition); // Required for odometry, use faster rate
        BaseStatusSignal.setUpdateFrequencyForAll(
                50.0,
                driveVelocity,
                driveAppliedVolts,
                driveCurrent,
                turnAbsolutePosition,
                turnVelocity,
                turnAppliedVolts,
                turnCurrent);
    }

    @Override
    public void updateInputs(AndromedaModuleIOInputs inputs) {
        BaseStatusSignal.refreshAll(
                drivePosition,
                driveVelocity,
                driveAppliedVolts,
                driveCurrent,
                turnAbsolutePosition,
                turnPosition,
                turnVelocity,
                turnAppliedVolts,
                turnCurrent);

        inputs.driveVelocity = driveVelocity.getValueAsDouble();
        inputs.drivePosition = Units.rotationsToRadians(drivePosition.getValueAsDouble()) * andromedaModuleConfig.wheelCircumference;
        inputs.steerAngle = Rotation2d.fromRotations(turnPosition.getValueAsDouble());
        inputs.encoderAbsolutePosition = Rotation2d.fromRotations(turnAbsolutePosition.getValueAsDouble());   
    }

    @Override
    public void setTurnPosition(Rotation2d angle) {
        steeringMotor.setControl(angleSetter.withPosition(angle.getRotations()));
    }

    @Override
    public void setDriveSpeed(SwerveModuleState speed) {
        driveSetter.Velocity = Conversions.MPSToRPS(speed.speedMetersPerSecond,
                andromedaModuleConfig.wheelCircumference);
        driveSetter.FeedForward = feedforward.calculate(speed.speedMetersPerSecond);
        driveMotor.setControl(driveSetter);
    }

    @Override
    public void setDrivePercent(double percent) {
        driveDutyCycle.Output = percent;
        driveMotor.setControl(driveDutyCycle);
    }

    public Rotation2d getAbsoluteRotations() {
        return Rotation2d.fromRotations(steeringEncoder.getAbsolutePosition().getValue());
    }

    private void resetAbsolutePosition() {
        double absolutePosition = getAbsoluteRotations().getRotations();
        steeringMotor.setPosition(absolutePosition);
    }
}

// TODO REMOVE
/*
 * if (isOpenLoop) {
 * double percentOutput = desiredState.speedMetersPerSecond /
 * andromedaSwerveConfig.maxSpeed;
 * 
 * io.setDrivePercent(percentOutput);
 * } else {
 * double velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond,
 * this.andromedaModuleConfig.wheelCircumference);
 * double feedforward =
 * motorFeedforward.calculate(desiredState.speedMetersPerSecond);
 * 
 * io.setDriveVelocity(feedforward);
 * }
 */

/*
 * Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <=
 * (andromedaSwerveConfig.maxSpeed * 0.01))
 * ? lastAngle
 * : desiredState.angle;
 */

// TODO REMOVE
/*
 * if (isOpenLoop) {
 * double percentOutput = desiredState.speedMetersPerSecond /
 * andromedaSwerveConfig.maxSpeed;
 * 
 * io.setDrivePercent(percentOutput);
 * } else {
 * double velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond,
 * this.andromedaModuleConfig.wheelCircumference);
 * double feedforward =
 * motorFeedforward.calculate(desiredState.speedMetersPerSecond);
 * 
 * io.setDriveVelocity(feedforward);
 * }
 */