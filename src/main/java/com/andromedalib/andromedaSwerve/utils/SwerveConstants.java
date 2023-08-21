package com.andromedalib.andromedaSwerve.utils;

import com.andromedalib.andromedaSwerve.utils.AndromedaProfileConfig.AndromedaProfiles;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public class SwerveConstants {
    /* Represents Swerve-wide constants */

    public static final double deadband = 0.1;
    public static final AndromedaProfileConfig andromedaProfile = AndromedaProfileConfig
            .getConfig(AndromedaProfiles.ANDROMEDA_CONFIG);

    /* Drivetrain Constants */
    public static final double trackWidth = Units.inchesToMeters(18.5); 
    public static final double wheelBase = Units.inchesToMeters(18.5);
    public static final double wheelCircumference = andromedaProfile.wheelCircumference;
    public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0));

    /*
     * Drive Motor Characterization Values
     * Divide SYSID values by 12 to convert from volts to percent output for CTRE
     */
    public static final double driveKS = (0.32 / 12); // TODO TUNE
    public static final double driveKV = (1.51 / 12);
    public static final double driveKA = (0.27 / 12);

    /* Swerve Profiling Values */
    /** Meters per Second */
    public static final double maxSpeed = 5.4864;
    public static final double maxAcceleration = maxSpeed * 0.85;
    /** Radians per Second */
    public static final double maxAngularVelocity = 11.5;
    public static final double maxAngularAcceleration = 3.5;

}
