/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.andromedaModule;

import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.SuperTalonFX;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.andromedalib.sensors.SuperCANCoder;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.andromedalib.andromedaSwerve.utils.AndromedaModuleConstants;
import com.andromedalib.andromedaSwerve.utils.AndromedaProfileConfig;
import com.andromedalib.andromedaSwerve.utils.AndromedaState;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

public class FalconAndromedaModule implements AndromedaModule {
        private int moduleNumber;
        private String moduleName;

        private SuperTalonFX driveMotor;
        private SuperTalonFX steeringMotor;
        private SuperCANCoder steeringEncoder;

        private Rotation2d lastAngle;

        private PositionVoltage angleSetter = new PositionVoltage(0).withSlot(0);
        private VelocityVoltage driveSetter = new VelocityVoltage(0).withSlot(0);

        private SwerveModuleState moduleDesiredState;

        private AndromedaProfileConfig andromedaProfile;

        private NetworkTable swerveModuleTable;
        private DoubleEntry speedEntry;
        private DoubleEntry angleEntry;
        private DoubleEntry encoderAngle;
        private DoubleEntry desiredSpeedEntry;
        private DoubleEntry desiredAngleEntry;

        private SimpleMotorFeedforward feedforward;

        /**
         * Creates a new FalconAndromedaModule, that uses {@link SuperTalonFX} Motors
         * 
         * @param moduleNumber This module's number
         * @param moduleName   This module's name
         * @param constants    IDs and offsets constants
         */
        public FalconAndromedaModule(int moduleNumber, String moduleName, AndromedaModuleConstants constants,
                        AndromedaProfileConfig config) {
                this.moduleNumber = moduleNumber;
                this.moduleName = moduleName;
                this.andromedaProfile = config;

                if (andromedaProfile.motorConfig.equals("Neo config")) {
                        DriverStation.reportError("AndromedaModule " + moduleNumber
                                        + " is using Neo config. Please change your profile config selection to avoid unwanted behaviours",
                                        true);
                }

                this.driveMotor = new SuperTalonFX(constants.driveMotorID, GlobalIdleMode.brake,
                                andromedaProfile.driveMotorInvert,
                                andromedaProfile.driveMotorConfiguration,
                                andromedaProfile.CANbus);
                this.steeringMotor = new SuperTalonFX(constants.steeringMotorID, GlobalIdleMode.Coast,
                                andromedaProfile.steeringMotorInvert,
                                andromedaProfile.turningMotorConfiguration,
                                andromedaProfile.CANbus);

                andromedaProfile.cancoderConfig.MagnetSensor.MagnetOffset = constants.angleOffset.getDegrees();

                this.steeringEncoder = new SuperCANCoder(constants.absCanCoderID,
                                andromedaProfile.cancoderConfig,
                                andromedaProfile.CANbus);

                feedforward = new SimpleMotorFeedforward(andromedaProfile.driveMotorConfiguration.Slot0.kS,
                                andromedaProfile.driveMotorConfiguration.Slot0.kA,
                                andromedaProfile.driveMotorConfiguration.Slot0.kV);

                resetAbsolutePosition();

                lastAngle = getAngle();

                moduleDesiredState = new SwerveModuleState(0.0, new Rotation2d(0.0));

                swerveModuleTable = NetworkTableInstance.getDefault()
                                .getTable("AndromedaSwerveTable/SwerveModule/" + moduleNumber);
                speedEntry = swerveModuleTable.getDoubleTopic("Speed").getEntry(getState().speedMetersPerSecond);
                angleEntry = swerveModuleTable.getDoubleTopic("Angle").getEntry(getAngle().getDegrees());
                encoderAngle = swerveModuleTable.getDoubleTopic("Encoder")
                                .getEntry(steeringEncoder.getAbsolutePositionDegrees());
                desiredSpeedEntry = swerveModuleTable.getDoubleTopic("DesiredSpeed")
                                .getEntry(getDesiredState().speedMetersPerSecond);
                desiredAngleEntry = swerveModuleTable.getDoubleTopic("DesiredAngle")
                                .getEntry(getDesiredState().angle.getDegrees());

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
                desiredState = AndromedaState.optimize(desiredState, getState().angle);

                moduleDesiredState = desiredState;

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
                steeringMotor.setControl(angleSetter.withPosition(
                                Conversions.degreesToFalcon(angle.getDegrees(), andromedaProfile.steeringGearRatio)));

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

                        driveMotor.setControl(driveSetter.withVelocity(percentOutput));
                } else {
                        double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                                        andromedaProfile.wheelCircumference,
                                        1);
                        driveMotor.setControl(driveSetter.withFeedForward(
                                        feedforward.calculate(velocity)));
                }
        }

        @Override
        public void resetAbsolutePosition() {
                double encoderPosition = Conversions.degreesToFalcon(
                                steeringEncoder.getAbsolutePositionDegrees(),
                                andromedaProfile.steeringGearRatio);
 
                steeringMotor.setPosition(encoderPosition);
        }

        /**
         * Gets the current module angle
         * 
         * @return Current {@link Rotation2D}
         */
        private Rotation2d getAngle() {
                return Rotation2d.fromDegrees(
                                steeringMotor.getPosition().getValueAsDouble());
        }

        @Override
        public SwerveModuleState getState() {
                return new SwerveModuleState(driveMotor.getVelocity().getValueAsDouble(), getAngle());
        }

        @Override
        public SwerveModuleState getDesiredState() {
                return moduleDesiredState;
        }

        @Override
        public double[] getDoubleStates() {
                return new double[] { getState().speedMetersPerSecond, getState().angle.getDegrees() };
        }

        @Override
        public double[] getDoubleDesiredStates() {
                return new double[] { getDesiredState().speedMetersPerSecond, getDesiredState().angle.getDegrees() };
        }

        @Override
        public SwerveModulePosition getPosition() {
                return new SwerveModulePosition(
                                driveMotor.getPosition(SwerveConstants.wheelCircumference,
                                                andromedaProfile.driveGearRatio),
                                getAngle());
        }

        /* Telemetry */

        @Override
        public double[] getTemp() {
                return new double[] { steeringMotor.getDeviceTemp().getValue(), driveMotor.getDeviceTemp().getValue() };
        }

        @Override
        public void updateNT() {
                speedEntry.set(getState().speedMetersPerSecond);
                angleEntry.set(getAngle().getDegrees());
                encoderAngle.set(steeringEncoder.getAbsolutePositionDegrees());
                desiredSpeedEntry.set(getDesiredState().speedMetersPerSecond);
                desiredAngleEntry.set(getDesiredState().angle.getDegrees());
        }
}
