package com.andromedalib.shuffleboard;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ShuffleboardInteractions {

    public static ShuffleboardInteractions instance;

    public static String tabName;
    private static ShuffleboardTab tab;

    private ShuffleboardInteractions() {
    }

    public static ShuffleboardInteractions getInstance(String name) {
        if (instance == null) {
            instance = new ShuffleboardInteractions();
            tabName = name;
            tab = Shuffleboard.getTab(tabName);
        }
        return instance;
    }

    public static ShuffleboardInteractions getInstance(ShuffleboardTab shuffleTab) {
        if (instance == null) {
            instance = new ShuffleboardInteractions();
            tab = shuffleTab;
        }
        return instance;
    }


    public void addTab(String name, Sendable sendable) {
        tab.add(name, sendable);
    }

    public void addTab(String name, Sendable sendable, int width, int height) {
        tab.add(name, sendable).withSize(width, height);
    }

    public void addTab(String name, Sendable sendable, int width, int height, int x, int y) {
        tab.add(name, sendable).withSize(width, height).withPosition(x, y);
    }
}
