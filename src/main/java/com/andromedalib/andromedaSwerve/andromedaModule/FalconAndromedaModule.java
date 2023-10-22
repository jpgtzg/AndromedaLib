/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.andromedaModule;

import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.SuperTalonFX;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.andromedalib.sensors.SuperCANCoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FalconAndromedaModule implements AndromedaModule {
        private int moduleNumber;
        private String moduleName;

        private SuperTalonFX driveMotor;
        private SuperTalonFX steeringMotor;
        private SuperCANCoder steeringEncoder;

        private Rotation2d angleOffset;
        private Rotation2d lastAngle;

        private SwerveModuleState moduleDesiredState;

        private AndromedaProfileConfig andromedaProfile;

        private NetworkTable swerveModuleTable;
        private DoubleEntry speedEntry;
        private DoubleEntry angleEntry;
        private DoubleEntry encoderAngle;
        private DoubleEntry desiredSpeedEntry;
        private DoubleEntry desiredAngleEntry;

        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SwerveConstants.driveKS,
                        SwerveConstants.driveKV,
                        SwerveConstants.driveKA);

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
                this.steeringEncoder = new SuperCANCoder(constants.absCanCoderID,
                                andromedaProfile.cancoderConfig,
                                andromedaProfile.CANbus);

                this.angleOffset = constants.angleOffset;

                steeringEncoder.setPositionToAbsolute();
                
                resetAbsolutePosition();

                lastAngle = getAngle();

                moduleDesiredState = new SwerveModuleState(0.0, new Rotation2d(0.0));

                swerveModuleTable = NetworkTableInstance.getDefault()
                                .getTable("AndromedaSwerveTable/SwerveModule/" + moduleNumber);
                speedEntry = swerveModuleTable.getDoubleTopic("Speed").getEntry(getState().speedMetersPerSecond);
                angleEntry = swerveModuleTable.getDoubleTopic("Angle").getEntry(getAngle().getDegrees());
                encoderAngle = swerveModuleTable.getDoubleTopic("Encoder")
                                .getEntry(steeringEncoder.getAbsolutePosition());
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

                SmartDashboard.putNumber(getModuleName(), angle.getDegrees());
                steeringMotor.set(ControlMode.Position,
                                Conversions.degreesToFalcon(angle.getDegrees(),
                                                andromedaProfile.steeringGearRatio));
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

                        driveMotor.set(ControlMode.PercentOutput, percentOutput);
                } else {
                        double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                                        andromedaProfile.wheelCircumference,
                                        andromedaProfile.driveGearRatio);
                        steeringMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward,
                                        feedforward.calculate(desiredState.speedMetersPerSecond));
                }
        }

        @Override
        public void resetAbsolutePosition() {
                steeringMotor.setSelectedSensorPosition(0);
                double encoderPosition = Conversions.degreesToFalcon(
                                steeringEncoder.getAbsolutePosition() - angleOffset.getDegrees(),
                                andromedaProfile.steeringGearRatio);
                try {
                        Thread.sleep(1000);
                } catch (Exception e) {

                }
                steeringMotor.setSelectedSensorPosition(encoderPosition); // Encoder positions
        }

        /**
         * Gets the current module angle
         * 
         * @return Current {@link Rotation2D}
         */
        private Rotation2d getAngle() {
                return Rotation2d.fromDegrees(
                                steeringMotor.getAngle(andromedaProfile.steeringGearRatio));
        }

        @Override
        public SwerveModuleState getState() {
                return new SwerveModuleState(driveMotor.getVelocity(SwerveConstants.wheelCircumference,
                                andromedaProfile.driveGearRatio), getAngle());
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
                return new double[] { steeringMotor.getTemperature(), driveMotor.getTemperature() };
        }

        @Override
        public void updateNT() {
                speedEntry.set(getState().speedMetersPerSecond);
                angleEntry.set(getAngle().getDegrees());
                encoderAngle.set(steeringEncoder.getAbsolutePosition());
                desiredSpeedEntry.set(getDesiredState().speedMetersPerSecond);
                desiredAngleEntry.set(getDesiredState().angle.getDegrees());
        }
}
