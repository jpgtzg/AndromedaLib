package com.andromedalib.shuffleboard;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class MotorInfoTab extends ShuffleboardTabBase {
    private static MotorInfoTab instance;

    private MotorInfoTab() {
    }

    /**
     * Initializes a new {@link MotorInfoTab}
     * 
     * @param name Name of the ShuffleboardTab
     * @return {@link MotorInfoTab} singleton instance
     */
    public static MotorInfoTab getInstance(String name) {
        if (instance == null) {
            instance = new MotorInfoTab();
            tabName = name;
            tab = Shuffleboard.getTab(tabName);
        }
        return instance;
    }

    /**
     * Initializes a new {@link MotorInfoTab}
     * 
     * @param shuffleTab ShuffleboardTab to be used
     * @return {@link MotorInfoTab} singleton instance
     */
    public static MotorInfoTab getInstance(ShuffleboardTab shuffleTab) {
        if (instance == null) {
            instance = new MotorInfoTab();
            tab = shuffleTab;
        }
        return instance;
    }

    /**
     * Returns the {@link MotorInfoTab} instance
     * 
     * @return {@link MotorInfoTab} singleton instance
     */
    public static MotorInfoTab getInstance() {
        return instance;
    }

}
