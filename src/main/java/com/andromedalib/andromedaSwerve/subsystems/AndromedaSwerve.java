/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModule;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;
import com.andromedalib.sensors.SuperNavx;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AndromedaSwerve extends SubsystemBase {
  private static AndromedaSwerve instance;

  private static NetworkTable andromedaSwerveTable;
  private static DoubleEntry navxPublisher;
  private static DoubleArrayEntry realSwerveStatesPublisher;
  private static DoubleArrayEntry desiredSwerveStatesPublisher;

  private AndromedaModule[] modules;
  public AndromedaSwerveConfig andromedaProfile;

  private static SuperNavx navx = SuperNavx.getInstance();

  private AndromedaSwerve(AndromedaModule[] modules, AndromedaSwerveConfig profileConfig) {
    this.andromedaProfile = profileConfig;
    this.modules = modules;

    andromedaSwerveTable = NetworkTableInstance.getDefault().getTable("AndromedaSwerveTable");
    navxPublisher = andromedaSwerveTable.getDoubleTopic("SwerveHeading").getEntry(getSwerveAngle().getDegrees());
    realSwerveStatesPublisher = andromedaSwerveTable.getDoubleArrayTopic("RealSwerveStates").getEntry(new double[] {});
    desiredSwerveStatesPublisher = andromedaSwerveTable.getDoubleArrayTopic("DesiredSwerveStates")
        .getEntry(new double[] {});
  }

  public static AndromedaSwerve getInstance(AndromedaModule[] modules, AndromedaSwerveConfig profileConfig) {
    if (instance == null) {
      instance = new AndromedaSwerve(modules, profileConfig);
    }
    return instance;
  }

  @Override
  public void periodic() {
    updateNT();
  }

  /**
   * Resets all modules to the absolute encoder position
   */
  public void resetAbsoluteModules() {
    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.resetAbsolutePosition();
    }
  }

  /**
   * Main driving method
   * 
   * @param translation   Translation movement
   * @param rotation      Desired rotation
   * @param fieldRelative True if field relative
   * @param isOpenLoop    True if open loop
   */
  public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates = andromedaProfile.swerveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                getSwerveAngle())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));

    setModuleStates(swerveModuleStates, isOpenLoop);
  }

  /**
   * Gets Navx Angle clamped to 0 - 360 degrees
   * 
   * @return
   */
  public Rotation2d getSwerveAngle() {
    return navx.getClampedYaw();
  }

  /**
   * Return all module states
   * 
   * @return SwerveModuleStates array
   */
  public SwerveModuleState[] getStates() {

    SwerveModuleState[] states = new SwerveModuleState[modules.length];
    for (AndromedaModule andromedaModule : modules) {
      states[andromedaModule.getModuleNumber()] = andromedaModule.getState();
    }

    return states;
  }

  public double[] getDoubleStates() {
    double[] states = new double[8];

    for (int i = 0; i < 4; i++) {
      double[] moduleStates = modules[i].getDoubleStates();
      states[i * 2] = moduleStates[0];
      states[i * 2 + 1] = moduleStates[1];
    }

    return states;
  }

  public double[] getDoubleDesiredStates() {
    double[] states = new double[8];

    for (int i = 0; i < 4; i++) {
      double[] moduleStates = modules[i].getDoubleDesiredStates();
      states[i * 2] = moduleStates[0];
      states[i * 2 + 1] = moduleStates[1];
    }

    return states;
  }

  /**
   * Returns all module positions
   * 
   * @return SwerveModulePosition array
   */
  public SwerveModulePosition[] getPositions() {

    SwerveModulePosition[] states = new SwerveModulePosition[modules.length];
    for (AndromedaModule andromedaModule : modules) {
      states[andromedaModule.getModuleNumber()] = andromedaModule.getPosition();
    }

    return states;
  }

  public void resetNavx() {
    navx.reset();
  }

  /**
   * Gets the list of modules
   * 
   * @return List of modules
   */
  public List<AndromedaModule> getModules() {
    List<AndromedaModule> modulesList = Arrays.asList(modules);
    return modulesList;
  }

  /**
   * Sets the modules states as open loop driving
   * 
   * @param desiredStates Desired SwerveModuleState array
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    setModuleStates(desiredStates, true);
  }

  /**
   * Sets the modules states
   * 
   * @param desiredStates Desired SwerveModuleState array
   * @param isOpenLoop    True if open loop driving
   */
  public void setModuleStates(SwerveModuleState[] desiredStates, boolean isOpenLoop) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, andromedaProfile.maxSpeed);

    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.setDesiredState(desiredStates[andromedaModule.getModuleNumber()], isOpenLoop);
    }
  }

  /**
   * Gets each modules' motors temperature
   * 
   * @return ArrayList module temperature
   */
  public ArrayList<Double> getModulesTemp() {

    ArrayList<Double> temps = new ArrayList<>();

    for (AndromedaModule andromedaModule : modules) {
      for (int i = 0; i < 2; i++) {
        temps.add(andromedaModule.getTemp()[i]);
      }
    }
    return temps;
  }

  /* Telemetry */

  public void updateNT() {
    navxPublisher.set(getSwerveAngle().getDegrees());

    realSwerveStatesPublisher.set(getDoubleStates());
    desiredSwerveStatesPublisher.set(getDoubleDesiredStates());

    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.updateNT();
    }
  }
}
