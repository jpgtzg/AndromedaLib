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
import com.andromedalib.andromedaSwerve.utils.AndromedaState;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
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

        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SwerveConstants.driveKS,
                        SwerveConstants.driveKV,
                        SwerveConstants.driveKA);

        public FalconAndromedaModule(int moduleNumber, String moduleName, AndromedaModuleConstants constants) {
                this.moduleNumber = moduleNumber;
                this.moduleName = moduleName;

                if (SwerveConstants.andromedaProfile.motorConfig.equals("Neo config")) {
                        DriverStation.reportError("AndromedaModule " + moduleNumber
                                        + " is using Neo config. Please change your profile config selection to avoid unwanted behaviours",
                                        true);
                }

                this.driveMotor = new SuperTalonFX(constants.driveMotorID, GlobalIdleMode.brake,
                                SwerveConstants.andromedaProfile.driveMotorInvert,
                                SwerveConstants.andromedaProfile.driveMotorConfiguration,
                                SwerveConstants.andromedaProfile.CANbus);
                this.steeringMotor = new SuperTalonFX(constants.steeringMotorID, GlobalIdleMode.Coast,
                                SwerveConstants.andromedaProfile.steeringMotorInvert,
                                SwerveConstants.andromedaProfile.turningMotorConfiguration,
                                SwerveConstants.andromedaProfile.CANbus);
                this.steeringEncoder = new SuperCANCoder(constants.absCanCoderID,
                                SwerveConstants.andromedaProfile.cancoderConfig,
                                SwerveConstants.andromedaProfile.CANbus);

                this.angleOffset = constants.angleOffset;

                resetAbsolutePosition();

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
                desiredState = AndromedaState.optimize(desiredState, getState().angle);

                setAngle(desiredState);
                setSpeed(desiredState, isOpenLoop);
        }

        private void setAngle(SwerveModuleState desiredState) {
                Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConstants.maxSpeed * 0.01))
                                ? lastAngle
                                : desiredState.angle;

                SmartDashboard.putNumber(getModuleName(), angle.getDegrees());
                steeringMotor.set(ControlMode.Position,
                                Conversions.degreesToFalcon(angle.getDegrees(),
                                                SwerveConstants.andromedaProfile.steeringGearRatio));
                lastAngle = angle;
        }

        private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
                if (isOpenLoop) {
                        double percentOutput = desiredState.speedMetersPerSecond / SwerveConstants.maxSpeed;

                        driveMotor.set(ControlMode.PercentOutput, percentOutput);
                } else {
                        double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                                        SwerveConstants.andromedaProfile.wheelCircumference,
                                        SwerveConstants.andromedaProfile.driveGearRatio);
                        steeringMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward,
                                        feedforward.calculate(desiredState.speedMetersPerSecond));
                }
        }

        @Override
        public void resetAbsolutePosition() {
                steeringMotor.setSelectedSensorPosition(0);
                double encoderPosition = Conversions.degreesToFalcon(
                                steeringEncoder.getAbsolutePosition() - angleOffset.getDegrees(),
                                SwerveConstants.andromedaProfile.steeringGearRatio);
                try {
                        Thread.sleep(1000);
                } catch (Exception e) {

                }
                steeringMotor.setSelectedSensorPosition(encoderPosition); // Encoder positions
        }

        private Rotation2d getAngle() {
                return Rotation2d.fromDegrees(
                                steeringMotor.getAngle(SwerveConstants.andromedaProfile.steeringGearRatio));
        }

        @Override
        public SwerveModuleState getState() {
                return new SwerveModuleState(driveMotor.getVelocity(SwerveConstants.wheelCircumference,
                                SwerveConstants.andromedaProfile.driveGearRatio), getAngle());
        }

        @Override
        public SwerveModulePosition getPosition() {
                return new SwerveModulePosition(
                                driveMotor.getPosition(SwerveConstants.wheelCircumference,
                                                SwerveConstants.andromedaProfile.driveGearRatio),
                                getAngle());
        }

        /* Telemetry */

        @Override
        public double[] getTemp() {
                return new double[] { steeringMotor.getTemperature(), driveMotor.getTemperature() };
        }
}
