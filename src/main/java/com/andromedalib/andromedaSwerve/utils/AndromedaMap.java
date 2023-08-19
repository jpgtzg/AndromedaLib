/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.andromedaSwerve.utils;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * References the IDs of the motors & angle offsets
 */
public class AndromedaMap {

        /* IDs are set clockwise starting from front right */
        public static final int steerID1 = 1;
        public static final int driveID1 = 2;
        public static final int absEncoder1 = 3;
        public static final Rotation2d mod1Angleoffset = Rotation2d.fromDegrees(292.852);
        public static final AndromedaModuleConstants mod1Const = new AndromedaModuleConstants(steerID1, driveID1,
                        absEncoder1, mod1Angleoffset);

        public static final int steerID2 = 4;
        public static final int driveID2 = 5;
        public static final int absEncoder2 = 6;
        public static final Rotation2d mod2Angleoffset = Rotation2d.fromDegrees(88.330);
        public static final AndromedaModuleConstants mod2Const = new AndromedaModuleConstants(steerID2, driveID2,
                        absEncoder2, mod2Angleoffset);

        public static final int steerID3 = 7;
        public static final int drivelID3 = 8;
        public static final int absEncoder3 = 9;
        public static final Rotation2d mod3Angleoffset = Rotation2d.fromDegrees(141.152);
        public static final AndromedaModuleConstants mod3Const = new AndromedaModuleConstants(steerID3, drivelID3,
                        absEncoder3, mod3Angleoffset);

        public static final int steerID4 = 10;
        public static final int driveID4 = 11;
        public static final int absEncoder4 = 12;
        public static final Rotation2d mod4Angleoffset = Rotation2d.fromDegrees(74.707);
        public static final AndromedaModuleConstants mod4Const = new AndromedaModuleConstants(steerID4, driveID4,
                        absEncoder4, mod4Angleoffset);
}
