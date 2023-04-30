/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.shuffleboard;

import com.team6647.utils.shuffleboard.AutoModeSelector.AutoSelection;
import com.team6647.utils.shuffleboard.GridPlacementSelector.GridPlacement;

import edu.wpi.first.wpilibj2.command.Command;

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

    public AutoSelection getAutoMode() {
        return null;
    }

    public Command getDriveSelection() {
        return null;
    }

    public GridPlacement getGridPlacementSelection() {
        return null;
    }
}
