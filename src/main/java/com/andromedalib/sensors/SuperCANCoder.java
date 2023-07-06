/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.sensors;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;

import edu.wpi.first.math.geometry.Rotation2d;

public class SuperCANCoder extends CANCoder {

    /**
     * SuperCANCoder constructor
     * 
     * @param deviceID      CANCoder DeviceID
     * @param configuration Configuration
     */
    public SuperCANCoder(int deviceID, CANCoderConfiguration configuration) {
        super(deviceID);

        this.configFactoryDefault();
        this.configAllSettings(configuration);
    }

    /**
     * SuperCANCoder constructor to a defined canbus
     * 
     * @param deviceID      CANCoder DeviceID
     * @param configuration Configuration
     * @param canbus        Device canbus
     */
    public SuperCANCoder(int deviceID, CANCoderConfiguration configuration, String canbus) {
        super(deviceID, canbus);

        this.configFactoryDefault();
        this.configAllSettings(configuration);
    }

    /**
     * Gets the absolute degrees position
     * 
     * @return Absolute position in degrees
     */
    public double getDegrees() {
        return getRotation().getDegrees();
    }

    /**
     * Gets the absolute {@link Rotation2d} positions
     * 
     * @return Absolute positions
     */
    public Rotation2d getRotation() {
        return Rotation2d.fromDegrees(getAbsolutePosition());
    }
}
