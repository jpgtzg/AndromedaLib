/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.utils;

import com.andromedalib.motorControllers.SuperSparkMax;
import com.andromedalib.motorControllers.SuperTalonFX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.util.Units;

/* Represents an entire profile configuration for a module */
public final class AndromedaProfileConfig {

        public final double steeringGearRatio;
        public final double driveGearRatio;

        public final double wheelDiameter;
        public final double wheelCircumference;

        public final boolean driveMotorInvert;
        public final boolean steeringMotorInvert;

        public final CANcoderConfiguration cancoderConfig;
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

        /**
         * Creates a new AndromedaProfileConfig. This constructor is aimed for use with
         * {@link SuperTalonFX} motors
         * 
         * @param steeringGearRatio         Gear ratio between the steering motor and
         *                                  the wheel
         * @param driveGearRatio            Gear Ratio between the drive motor and the
         *                                  wheel
         * @param wheelDiameter             Wheel diameter
         * @param driveMotorConfiguration   {@link TalonFXConfiguration} for the drive
         *                                  motor
         * @param turningMotorConfiguration {@link TalonFXConfiguration} for the turning
         *                                  motor
         * @param cancoderConfiguration     {@link CANCoderConfiguration} for the
         *                                  CANCoder
         * @param driveMotorInvert          Drive motor invert status
         * @param steeringMotorInvert       Steering motor invert status
         * @param cancoderInvert            CANCoder motor invert status
         * @param CANBus                    CANbus name
         */
        public AndromedaProfileConfig(double wheelDiameter,
                        TalonFXConfiguration driveMotorConfiguration, TalonFXConfiguration turningMotorConfiguration,
                        CANcoderConfiguration cancoderConfiguration,
                        String CANBus) {
                this.wheelDiameter = wheelDiameter;
                this.wheelCircumference = wheelDiameter * Math.PI;

                this.driveMotorConfiguration = driveMotorConfiguration;
                this.turningMotorConfiguration = turningMotorConfiguration;

                this.cancoderConfig = cancoderConfiguration;
                this.CANbus = CANBus;

                /* Unused configs */

                this.driveMotorInvert = false;
                this.steeringMotorInvert = false;
                this.steeringGearRatio = 0;
                this.driveGearRatio = 0;

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

        /**
         * Creates a new AndromedaProfileConfig. This constructor is aimed for use with
         * {@link SuperSparkMax} motors
         * 
         * @param steeringGearRatio           Gear ratio between the steering motor and
         *                                    the wheel
         * @param driveGearRatio              Gear Ratio between the drive motor and the
         *                                    wheel
         * @param wheelDiameter               Wheel diameter
         * @param turningKp                   Kp value for turning motor
         * @param turningKi                   Ki value for turning motor
         * @param turningKd                   Kd value for turning motor
         * @param turningKf                   Kf value for turning motor
         * @param driveKp                     Kp value for drive motor
         * @param driveKi                     Ki value for drive motor
         * @param driveKd                     Kd value for drive motor
         * @param driveKf                     Kf value for drive motor
         * @param cancoderConfiguration       {@link CANCoderConfiguration} for the
         *                                    CANCoder
         * @param driveMotorInvertDrive       motor invert status
         * @param angleMotorInvert            Steering motor invert status
         * @param cancoderInvert              CANCoder motor invert status
         * @param driveContinuousCurrentLimit Drive motor current limit
         * @param angleContinuousCurrentLimit Angle motor current limit
         */
        public AndromedaProfileConfig(double steeringGearRatio, double driveGearRatio, double wheelDiameter,
                        double turningKp, double turningKi, double turningKd, double turningKf, double driveKp,
                        double driveKi, double driveKd, double driveKf, CANcoderConfiguration cancoderConfig,
                        boolean driveMotorInvert, boolean angleMotorInvert,
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

                double driveKp = 0.3;
                double driveKi = 0.0;
                double driveKd = 0.0;
                double driveKs = (0.32 / 12); // TODO TUNE
                double driveKv = (1.51 / 12); // TODO TUNE
                double driveKa = (0.27 / 12); // TODO TUNE

                InvertedValue driveMotorInvert = InvertedValue.CounterClockwise_Positive; // False
                InvertedValue angleMotorInvert = InvertedValue.Clockwise_Positive; // True
                SensorDirectionValue canCoderInvert = SensorDirectionValue.CounterClockwise_Positive; // False

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

                /* CANCoder */

                CANcoderConfiguration cancoderConfig = new CANcoderConfiguration();
                cancoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
                cancoderConfig.MagnetSensor.SensorDirection = canCoderInvert;

                /* Drive Motor */

                TalonFXConfiguration driveMotorConfig = new TalonFXConfiguration();
                driveMotorConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = openLoopRamp;
                driveMotorConfig.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = closedLoopRamp;
                
                driveMotorConfig.Slot0.kP = driveKp;
                driveMotorConfig.Slot0.kI = driveKi;
                driveMotorConfig.Slot0.kD = driveKd;
                driveMotorConfig.Slot0.kS = driveKs;
                driveMotorConfig.Slot0.kV = driveKv;
                driveMotorConfig.Slot0.kA = driveKa;

                driveMotorConfig.MotorOutput.Inverted = driveMotorInvert;

                driveMotorConfig.Feedback.SensorToMechanismRatio = driveGearRatio;
                driveMotorConfig.Audio.BeepOnBoot = true;
                driveMotorConfig.Audio.BeepOnConfig = true;

                driveMotorConfig.CurrentLimits.SupplyCurrentLimit = driveContinuousCurrentLimit;
                driveMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = driveEnableCurrentLimit;
                driveMotorConfig.CurrentLimits.SupplyCurrentThreshold = drivePeakCurrentLimit;
                driveMotorConfig.CurrentLimits.SupplyTimeThreshold = drivePeakCurrentDuration;

                /* Turning Motor */

                TalonFXConfiguration turningMotorConfig = new TalonFXConfiguration();
                turningMotorConfig.Slot0.kP = turningKp;
                turningMotorConfig.Slot0.kI = turningKi;
                turningMotorConfig.Slot0.kD = turningKd;

                turningMotorConfig.MotorOutput.Inverted = angleMotorInvert;

                turningMotorConfig.Feedback.SensorToMechanismRatio = steeringGearRatio;
                turningMotorConfig.Audio.BeepOnBoot = true;
                turningMotorConfig.Audio.BeepOnConfig = true;

                turningMotorConfig.CurrentLimits.SupplyCurrentLimit = angleContinuousCurrentLimit;
                turningMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = angleEnableCurrentLimit;
                turningMotorConfig.CurrentLimits.SupplyCurrentThreshold = anglePeakCurrentLimit;
                turningMotorConfig.CurrentLimits.SupplyTimeThreshold = anglePeakCurrentDuration;

                return new AndromedaProfileConfig(
                                wheelDiameter,
                                driveMotorConfig,
                                turningMotorConfig,
                                cancoderConfig,
                                swerveCANBus);
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
                SensorDirectionValue canCoderInvert = SensorDirectionValue.CounterClockwise_Positive; // False

                /* Current Limiting */
                int driveContinuousCurrentLimit = 35;

                int angleContinuousCurrentLimit = 25;

                /* CANCoder */

                CANcoderConfiguration cancoderConfig = new CANcoderConfiguration();
                cancoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
                cancoderConfig.MagnetSensor.SensorDirection = canCoderInvert;

                /* Drive Motor */

                /* Turning Motor */

                return new AndromedaProfileConfig(
                                steeringGearRatio,
                                driveGearRatio,
                                wheelDiameter,
                                turningKp, turningKi, turningKd, turningKf,
                                driveKp, driveKi, driveKd, driveKf, cancoderConfig,
                                driveMotorInvert, angleMotorInvert,
                                driveContinuousCurrentLimit, angleContinuousCurrentLimit);
        }
}
