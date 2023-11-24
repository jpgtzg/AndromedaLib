/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.sensors;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.geometry.Rotation2d;

public class SuperCANCoder extends CANcoder {

    /**
     * SuperCANCoder constructor
     * 
     * @param deviceID      CANCoder DeviceID
     * @param configuration Configuration
     */
    public SuperCANCoder(int deviceID, CANcoderConfiguration configuration) {
        super(deviceID);

        getConfigurator().apply(new CANcoderConfiguration());
        getConfigurator().apply(configuration);
    }

    /**
     * SuperCANCoder constructor to a defined canbus
     * 
     * @param deviceID      CANCoder DeviceID
     * @param configuration Configuration
     * @param canbus        Device canbus
     */
    public SuperCANCoder(int deviceID, CANcoderConfiguration configuration, String canbus) {
        super(deviceID, canbus);

        getConfigurator().apply(new CANcoderConfiguration());
        getConfigurator().apply(configuration);
    }

    /**
     * Gets the absolute degrees position
     * 
     * @return Absolute position in degrees
     */
    public double getDegrees() {
        return getRotation().getDegrees() * 360;
    }

    /**
     * Gets the absolute position in degrees
     * 
     * @return Absolute position in degrees
     */
    public double getAbsolutePositionDegrees(){
        return getAbsolutePosition().getValueAsDouble() * 360;
    }

    /**
     * Gets the absolute {@link Rotation2d} positions
     * 
     * @return Absolute positions
     */
    public Rotation2d getRotation() {
        return Rotation2d.fromDegrees(getAbsolutePosition().getValue());
    }
}
