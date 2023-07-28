/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.systems;

import java.util.ArrayList;

import com.andromedalib.sensors.SuperNavx;
import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModule;
import com.andromedalib.andromedaSwerve.utils.AndromedaMap;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AndromedaSwerve extends SubsystemBase {
  private static AndromedaSwerve instance;
  private static AndromedaModule[] modules;

  private static SuperNavx navx = SuperNavx.getInstance();

  private AndromedaSwerve() {

    modules = new AndromedaModule[] {
        new AndromedaModule(0, AndromedaMap.mod1Const),
        new AndromedaModule(1, AndromedaMap.mod2Const),
        new AndromedaModule(2, AndromedaMap.mod3Const),
        new AndromedaModule(3, AndromedaMap.mod4Const),
    };

    Timer.delay(1.0);
    resetAbsoluteModules();
  }

  public static AndromedaSwerve getInstance() {
    if (instance == null) {
      instance = new AndromedaSwerve();
    }
    return instance;
  }

  @Override
  public void periodic() {
  }

  public void resetAbsoluteModules() {
    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.resetAbsolutePosition();
    }
  }

  public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates = SwerveConstants.swerveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                navx.getClampedYaw())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));

    setModuleStates(swerveModuleStates, isOpenLoop);
  }

  private void setModuleStates(SwerveModuleState[] desiredStates, boolean isOpenLoop) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.maxSpeed);

    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.setDesiredState(desiredStates[andromedaModule.getModuleNumber()], isOpenLoop);
    }
  }

  public ArrayList<Double> getModulesTemp() {

    ArrayList<Double> temps = new ArrayList<>();

    for (AndromedaModule andromedaModule : modules) {
      for (int i = 0; i < 2; i++) {
        temps.add(andromedaModule.getTemp()[i]);
      }
    }
    return temps;
  }
}
