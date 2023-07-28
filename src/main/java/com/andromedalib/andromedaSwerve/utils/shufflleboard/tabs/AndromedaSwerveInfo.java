package com.andromedalib.andromedaSwerve.utils.shufflleboard.tabs;

import com.andromedalib.shuffleboard.ShuffleboardTabBase;
import com.andromedalib.andromedaSwerve.systems.AndromedaSwerve;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class AndromedaSwerveInfo extends ShuffleboardTabBase {
    AndromedaSwerve andromedaSwerve = AndromedaSwerve.getInstance();

    GenericEntry[] temps = new GenericEntry[8];

    int module = 1;
    int i = 0;

    public AndromedaSwerveInfo(ShuffleboardTab tab) {

        andromedaSwerve.getModulesTemp().forEach((temp) -> {
            temps[i] = tab.add("Module " + module + " " + i, temp).getEntry();
            if ((i + 1) % 2 == 0) {
                module = (module % 4) + 1;
            }
            i++;
        });
    }

    @Override
    public void updateTelemetry() {
        i = 0;
        andromedaSwerve.getModulesTemp().forEach((temp) -> {
            temps[i].setDouble(temp);
            i++;
        });
    }

}
