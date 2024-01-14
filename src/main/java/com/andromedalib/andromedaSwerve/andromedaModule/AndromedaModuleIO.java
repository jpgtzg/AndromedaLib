package com.andromedalib.andromedaSwerve.andromedaModule;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Rotation2d;

public interface AndromedaModuleIO {

    @AutoLog
    public static class AndromedaModuleIOInputs {
        public double driveVelocity = 0.0;
        public double steerAngle = 0.0;
        public double encoderAbsolutePosition = 0.0;

        /* Desired values */
        public double desiredDriveVelocity = 0.0;
        public double desiredAngle = 0.0;
    }

    /** Updates the set of loggable inputs. */
    public default void updateInputs(AndromedaModuleIOInputs inputs) {}

    /** Sets the turn motor to the position */
    public default void setTurnPosition(Rotation2d angle) {}

    /** Sets the drive motor to a speed */
    public default void setDriveSpeed(double percent, boolean isOpenLoop) {}

}
