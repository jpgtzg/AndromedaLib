/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andromedalib.sensors.SuperNavx;
import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModule;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AndromedaSwerve extends SubsystemBase {
  private static AndromedaSwerve instance;
  private AndromedaModule[] modules;

  private static SuperNavx navx = SuperNavx.getInstance();

  private AndromedaSwerve(AndromedaModule[] modules) {

    this.modules = modules;

    Timer.delay(1.0);
    resetAbsoluteModules();
  }

  public static AndromedaSwerve getInstance(AndromedaModule[] modules) {
    if (instance == null) {
      instance = new AndromedaSwerve(modules);
    }
    return instance;
  }

  @Override
  public void periodic() {
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
    SwerveModuleState[] swerveModuleStates = SwerveConstants.swerveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                navx.getClampedYaw())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));

    setModuleStates(swerveModuleStates, isOpenLoop);
  }

  /**
   * Gets Navx Angle clamped to 0 - 360 degrees
   * 
   * @return
   */
  public Rotation2d getAngle() {
    return navx.getClampedYaw();
  }

  /**
   * Return all module states
   * 
   * @return SwerveModuleStates array
   */
  public SwerveModuleState[] getStates() {

    SwerveModuleState[] states = new SwerveModuleState[4];
    for (AndromedaModule andromedaModule : modules) {
      states[andromedaModule.getModuleNumber()] = andromedaModule.getState();
    }

    return states;
  }

  /**
   * Returns all module positions
   * 
   * @return SwerveModulePosition array
   */
  public SwerveModulePosition[] getPositions() {

    SwerveModulePosition[] states = new SwerveModulePosition[4];
    for (AndromedaModule andromedaModule : modules) {
      states[andromedaModule.getModuleNumber()] = andromedaModule.getPosition();
    }

    return states;
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
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.maxSpeed);

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
}
