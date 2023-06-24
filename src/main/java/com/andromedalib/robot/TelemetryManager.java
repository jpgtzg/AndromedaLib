/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.robot;

/**
 * Telemetry manager class. Change functions and parameters to suit your needs.
 * It is automatically updated by the {@link SuperRobot} class
 * Handles selectors
 */
public class TelemetryManager {
    private static TelemetryManager instance;

    /**
     * Private Constructor
     */
    private TelemetryManager() {

    }

    public static TelemetryManager getInstance() {
        if (instance == null) {
            instance = new TelemetryManager();
        }
        return instance;
    }

    public void initTelemetry() {
    }

    public void updateTelemetry() {
    }
}
