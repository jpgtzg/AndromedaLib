/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.andromedaModule;

import com.andromedalib.andromedaSwerve.config.AndromedaModuleConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaModuleConfig.ModuleMotorConfig;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.andromedalib.motorControllers.SuperTalonFX;
import com.andromedalib.sensors.SuperCANCoder;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * AndromedaModule IO Implementation for TalonFX
 */
public class AndromedaModuleIOTalonFX implements AndromedaModuleIO {
        private SuperTalonFX driveMotor;
        private SuperTalonFX steeringMotor;
        private SuperCANCoder steeringEncoder;

        private AndromedaModuleConfig andromedaModuleConfig;

        private final StatusSignal<Double> drivePosition;
        private final StatusSignal<Double> driveVelocity;
        private final StatusSignal<Double> driveAppliedVolts;
        private final StatusSignal<Double> driveCurrent;

        private final StatusSignal<Double> turnAbsolutePosition;
        private final StatusSignal<Double> turnPosition;
        private final StatusSignal<Double> turnVelocity;
        private final StatusSignal<Double> turnAppliedVolts;
        private final StatusSignal<Double> turnCurrent;

        public AndromedaModuleIOTalonFX(int moduleNumber, AndromedaModuleConfig moduleConfig) {
                this.andromedaModuleConfig = moduleConfig;

                if (andromedaModuleConfig.motorConfig == ModuleMotorConfig.SPARKMAX_CONFIG) {
                        DriverStation.reportError("AndromedaModule " + moduleNumber
                                        + " is using Neo config. Please change your profile config selection to avoid unwanted behaviours",
                                        true);
                }

                this.driveMotor = new SuperTalonFX(andromedaModuleConfig.moduleIDs.driveMotorID, GlobalIdleMode.brake,
                                andromedaModuleConfig.driveMotorConfiguration,
                                andromedaModuleConfig.swerveCANBus);
                this.steeringMotor = new SuperTalonFX(andromedaModuleConfig.moduleIDs.steeringMotorID,
                                GlobalIdleMode.Coast,
                                andromedaModuleConfig.turningMotorConfiguration,
                                andromedaModuleConfig.swerveCANBus);

                this.steeringEncoder = new SuperCANCoder(andromedaModuleConfig.moduleIDs.absCanCoderID,
                                andromedaModuleConfig.cancoderConfiguration,
                                andromedaModuleConfig.swerveCANBus);

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
                inputs.drivePosition = Units.rotationsToRadians(drivePosition.getValueAsDouble())
                                * andromedaModuleConfig.wheelCircumference;
                inputs.steerAngle = Rotation2d.fromRotations(turnPosition.getValueAsDouble());
                inputs.encoderAbsolutePosition = Rotation2d.fromRotations(turnAbsolutePosition.getValueAsDouble());
                inputs.driveAppliedVolts = driveAppliedVolts.getValueAsDouble();

                inputs.turnVelocity = turnVelocity.getValueAsDouble();
                inputs.turnAppliedVolts = turnAppliedVolts.getValueAsDouble();
        }

        @Override
        public void setTurnVoltage(double volts) {
                steeringMotor.setControl(new VoltageOut(volts));
        }

        @Override
        public void setDriveVoltage(double volts) {
                steeringMotor.setControl(new VoltageOut(volts));
        }

        public Rotation2d getAbsoluteRotations() {
                return Rotation2d.fromRotations(steeringEncoder.getAbsolutePosition().getValue());
        }

        private void resetAbsolutePosition() {
                double absolutePosition = getAbsoluteRotations().getRotations();
                steeringMotor.setPosition(absolutePosition);
        }
}