/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.utils;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * Helper class, is used as a parameter when creating a new module
 */
public class AndromedaModuleConstants {
    public final int steeringMotorID;
    public final int driveMotorID;
    public final int absCanCoderID;
    public final Rotation2d angleOffset;

    public AndromedaModuleConstants(int steeringMotorID, int driveMotorID, int cancoderID,
            Rotation2d angleOffset) {
        this.steeringMotorID = steeringMotorID;
        this.driveMotorID = driveMotorID;
        this.absCanCoderID = cancoderID;
        this.angleOffset = angleOffset;
    }
}
