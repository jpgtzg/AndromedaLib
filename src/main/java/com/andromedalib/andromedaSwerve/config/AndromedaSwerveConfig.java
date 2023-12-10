/** Written by Juan Pablo Gutiérrez */

package com.andromedalib.andromedaSwerve.config;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

/* Represents the entire config */
public class AndromedaSwerveConfig {
    public final double deadband;

    public final double trackWidth;
    public final double wheelBase;

    public final SwerveDriveKinematics swerveKinematics;

    public final double maxSpeed;
    public final double maxAcceleration;

    public final double maxAngularVelocity;
    public final double maxAngularAcceleration;

    public AndromedaSwerveConfig(double deadband, double trackWidth, double wheelBase,
            SwerveDriveKinematics swerveKinematics, double maxSpeed, double maxAcceleration, double maxAngularVelocity,
            double maxAngularAcceleration) {
        this.deadband = deadband;
        this.trackWidth = trackWidth;
        this.wheelBase = wheelBase;
        this.swerveKinematics = swerveKinematics;
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
        this.maxAngularVelocity = maxAngularVelocity;
        this.maxAngularAcceleration = maxAngularAcceleration;
    }
}
