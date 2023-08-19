/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.utils;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;

import edu.wpi.first.math.util.Units;

/* Represents an entire profile configuration for a module */
public final class AndromedaProfileConfig {

        public final double steeringGearRatio;
        public final double driveGearRatio;

        public final double wheelDiameter;
        public final double wheelCircumference;

        public final boolean driveMotorInvert;
        public final boolean steeringMotorInvert;
        public final boolean canCoderInvert;

        public final CANCoderConfiguration cancoderConfig;
        public final TalonFXConfiguration driveMotorConfiguration;
        public final TalonFXConfiguration turningMotorConfiguration;

        public final String CANbus;

        /* SparkMax config variables */

        public final double turningKp;
        public final double turningKi;
        public final double turningKd;
        public final double turningKf;

        public final double driveKp;
        public final double driveKi;
        public final double driveKd;
        public final double driveKf;

        public final int driveContinuousCurrentLimit;
        public final int angleContinuousCurrentLimit;

        public final String motorConfig;

        public AndromedaProfileConfig(double steeringGearRatio, double driveGearRatio, double wheelDiameter,
                        TalonFXConfiguration driveMotorConfiguration, TalonFXConfiguration turningMotorConfiguration,
                        CANCoderConfiguration cancoderConfiguration,
                        boolean driveMotorInvert,
                        boolean steeringMotorInvert, boolean cancoderInvert, String CANBus) {
                this.steeringGearRatio = steeringGearRatio;
                this.driveGearRatio = driveGearRatio;
                this.wheelDiameter = wheelDiameter;
                this.wheelCircumference = wheelDiameter * Math.PI;

                this.driveMotorConfiguration = driveMotorConfiguration;
                this.turningMotorConfiguration = turningMotorConfiguration;

                this.cancoderConfig = cancoderConfiguration;

                this.driveMotorInvert = driveMotorInvert;
                this.steeringMotorInvert = steeringMotorInvert;
                this.canCoderInvert = cancoderInvert;
                this.CANbus = CANBus;

                this.turningKp = 0;
                this.turningKi = 0;
                this.turningKd = 0;
                this.turningKf = 0;

                this.driveKp = 0;
                this.driveKi = 0;
                this.driveKd = 0;
                this.driveKf = 0;

                this.driveContinuousCurrentLimit = 0;
                this.angleContinuousCurrentLimit = 0;

                this.motorConfig = "Falcon config";
        }

        public AndromedaProfileConfig(double steeringGearRatio, double driveGearRatio, double wheelDiameter,
                        double turningKp, double turningKi, double turningKd, double turningKf, double driveKp,
                        double driveKi, double driveKd, double driveKf, CANCoderConfiguration cancoderConfig,
                        boolean driveMotorInvert, boolean angleMotorInvert, boolean canCoderInvert,
                        int driveContinuousCurrentLimit, int angleContinuousCurrentLimit) {
                this.wheelDiameter = wheelDiameter;
                this.steeringGearRatio = steeringGearRatio;
                this.wheelCircumference = wheelDiameter * Math.PI;
                this.driveGearRatio = driveGearRatio;

                this.turningKp = turningKp;
                this.turningKi = turningKi;
                this.turningKd = turningKd;
                this.turningKf = turningKf;

                this.driveKp = driveKp;
                this.driveKi = driveKi;
                this.driveKd = driveKd;
                this.driveKf = driveKf;

                this.driveMotorInvert = driveMotorInvert;
                this.steeringMotorInvert = angleMotorInvert;
                this.canCoderInvert = canCoderInvert;

                this.driveContinuousCurrentLimit = driveContinuousCurrentLimit;
                this.angleContinuousCurrentLimit = angleContinuousCurrentLimit;

                this.CANbus = "null";
                this.driveMotorConfiguration = null;
                this.turningMotorConfiguration = null;
                this.cancoderConfig = cancoderConfig;

                this.motorConfig = "Neo config";
        }

        public static enum AndromedaProfiles {
                ANDROMEDA_CONFIG,
                NEO_CONFIG
        }

        /**
         * Gets the {@AndromedaProfileConfig} based on {@link} AndromedaProfiles}
         * selection
         * 
         * @param profile Profiles selection
         * @return {@link AndromedaProfileConfig} selection
         */
        public static AndromedaProfileConfig getConfig(AndromedaProfiles profile) {
                switch (profile) {
                        case ANDROMEDA_CONFIG:
                                return andromedaSwerveConfig();
                        case NEO_CONFIG:
                                return neoAndromedaSwerveConfig();
                }
                return andromedaSwerveConfig();
        }

        /* Base Andromeda Swerve configuration profile */
        private static AndromedaProfileConfig andromedaSwerveConfig() {

                String swerveCANBus = "6647_CANivore";
                double wheelDiameter = Units.inchesToMeters(4.0);

                double steeringGearRatio = ((150.0 / 7.0) / 1.0);
                double driveGearRatio = (6.75 / 1.0);

                double turningKp = 0.2;
                double turningKi = 0.0;
                double turningKd = 0.0;
                double turningKf = 0.0;

                double driveKp = 0.3;
                double driveKi = 0.0;
                double driveKd = 0.0;
                double driveKf = 0.0;

                boolean driveMotorInvert = true;
                boolean angleMotorInvert = true;
                boolean canCoderInvert = false;

                double openLoopRamp = 0.25;
                double closedLoopRamp = 0.0;

                /* Current Limiting */
                int driveContinuousCurrentLimit = 35;
                int drivePeakCurrentLimit = 60;
                double drivePeakCurrentDuration = 0.1;
                boolean driveEnableCurrentLimit = true;

                int angleContinuousCurrentLimit = 25;
                int anglePeakCurrentLimit = 40;
                double anglePeakCurrentDuration = 0.1;
                boolean angleEnableCurrentLimit = true;

                SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
                                driveEnableCurrentLimit,
                                driveContinuousCurrentLimit,
                                drivePeakCurrentLimit,
                                drivePeakCurrentDuration);

                SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
                                angleEnableCurrentLimit,
                                angleContinuousCurrentLimit,
                                anglePeakCurrentLimit,
                                anglePeakCurrentDuration);

                /* CANCoder */

                CANCoderConfiguration cancoderConfig = new CANCoderConfiguration();

                cancoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
                cancoderConfig.sensorDirection = canCoderInvert;
                cancoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
                cancoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;

                /* Drive Motor */

                TalonFXConfiguration driveMotorConfig = new TalonFXConfiguration();

                driveMotorConfig.slot0.kP = driveKp;
                driveMotorConfig.slot0.kI = driveKi;
                driveMotorConfig.slot0.kD = driveKd;
                driveMotorConfig.slot0.kF = driveKf;
                driveMotorConfig.supplyCurrLimit = driveSupplyLimit;
                driveMotorConfig.openloopRamp = openLoopRamp;
                driveMotorConfig.closedloopRamp = closedLoopRamp;

                /* Turning Motor */

                TalonFXConfiguration turningMotorConfig = new TalonFXConfiguration();

                turningMotorConfig.slot0.kP = turningKp;
                turningMotorConfig.slot0.kI = turningKi;
                turningMotorConfig.slot0.kD = turningKd;
                turningMotorConfig.slot0.kF = turningKf;
                turningMotorConfig.supplyCurrLimit = angleSupplyLimit;

                return new AndromedaProfileConfig(
                                steeringGearRatio,
                                driveGearRatio,
                                wheelDiameter,
                                driveMotorConfig,
                                turningMotorConfig, cancoderConfig,
                                driveMotorInvert, angleMotorInvert,
                                canCoderInvert, swerveCANBus);
        }

        /* Base Andromeda Swerve configuration profile */
        private static AndromedaProfileConfig neoAndromedaSwerveConfig() {

                double wheelDiameter = Units.inchesToMeters(4.0);

                double steeringGearRatio = ((150.0 / 7.0) / 1.0);
                double driveGearRatio = (6.75 / 1.0);

                double turningKp = 0.007;
                double turningKi = 0.0;
                double turningKd = 0.0;
                double turningKf = 0.0;

                double driveKp = 0.3;
                double driveKi = 0.0;
                double driveKd = 0.0;
                double driveKf = 0.0;

                boolean driveMotorInvert = false;
                boolean angleMotorInvert = true;
                boolean canCoderInvert = false;

                /* Current Limiting */
                int driveContinuousCurrentLimit = 35;

                int angleContinuousCurrentLimit = 25;

                /* CANCoder */

                CANCoderConfiguration cancoderConfig = new CANCoderConfiguration();

                cancoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
                cancoderConfig.sensorDirection = canCoderInvert;
                cancoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
                cancoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;

                /* Drive Motor */

                /* Turning Motor */

                return new AndromedaProfileConfig(
                                steeringGearRatio,
                                driveGearRatio,
                                wheelDiameter,
                                turningKp, turningKi, turningKd, turningKf,
                                driveKp, driveKi, driveKd, driveKf, cancoderConfig,
                                driveMotorInvert, angleMotorInvert,
                                canCoderInvert, driveContinuousCurrentLimit, angleContinuousCurrentLimit);
        }
}
