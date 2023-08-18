package com.andromedalib.andromedaSwerve.utils.shufflleboard.tabs;

import com.andromedalib.shuffleboard.ShuffleboardTabBase;
import com.andromedalib.andromedaSwerve.systems.AndromedaSwerve;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class AndromedaSwerveInfo extends ShuffleboardTabBase {
    AndromedaSwerve andromedaSwerve = AndromedaSwerve.getInstance();

    GenericEntry[] temps = new GenericEntry[8];
    GenericEntry[] angles = new GenericEntry[4];
    GenericEntry[] position = new GenericEntry[4];

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

        andromedaSwerve.getModules().forEach((module) -> {
            angles[module.getModuleNumber()] = tab
                    .add("Module " + module.getModuleNumber(), module.getState().angle.getDegrees())
                    .getEntry();
        });

        andromedaSwerve.getModules().forEach((module) -> {
            position[module.getModuleNumber()] = tab
                    .add("Module " + module.getModuleNumber(), module.getPosition().distanceMeters)
                    .getEntry();
        });
    }

    @Override
    public void updateTelemetry() {
        i = 0;
        andromedaSwerve.getModulesTemp().forEach((temp) -> {
            temps[i].setDouble(temp);
            i++;
        });

        andromedaSwerve.getModules().forEach((module) -> {
            angles[module.getModuleNumber()].setDouble(module.getState().angle.getDegrees());
        });

        andromedaSwerve.getModules().forEach((module) -> {
            position[module.getModuleNumber()].setDouble(module.getPosition().distanceMeters);
        });
    }

}
