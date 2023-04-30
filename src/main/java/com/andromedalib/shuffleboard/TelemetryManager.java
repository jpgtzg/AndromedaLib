/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.shuffleboard;

/**
 * Telemetry manager class. Change functions and parameters to suit your needs.
 * It is automatically updated by the {@link SuperRobot} class
 */
public class TelemetryManager {

    private static TelemetryManager instance;

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
