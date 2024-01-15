/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.subsystems;

import org.littletonrobotics.junction.Logger;
import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModule;
import com.andromedalib.andromedaSwerve.andromedaModule.GyroIO;
import com.andromedalib.andromedaSwerve.andromedaModule.GyroIOInputsAutoLogged;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AndromedaSwerve extends SubsystemBase {
  private static AndromedaSwerve instance;

  private AndromedaModule[] modules;
  public AndromedaSwerveConfig andromedaProfile;

  private final GyroIO gyroIO;
  private final GyroIOInputsAutoLogged gyroInputs = new GyroIOInputsAutoLogged();

/*   private SwerveDriveOdometry odometry;
 */
  private AndromedaSwerve(GyroIO gyro, AndromedaModule[] modules, AndromedaSwerveConfig profileConfig) {
    this.andromedaProfile = profileConfig;
    this.modules = modules;
    this.gyroIO = gyro;

/*     odometry = new SwerveDriveOdometry(profileConfig.swerveKinematics, getSwerveAngle(), getPositions());
 */  }

  public static AndromedaSwerve getInstance(GyroIO gyro, AndromedaModule[] modules,
      AndromedaSwerveConfig profileConfig) {
    if (instance == null) {
      instance = new AndromedaSwerve(gyro, modules, profileConfig);
    }
    return instance;
  }

  @Override
  public void periodic() {
    gyroIO.updateInputs(gyroInputs);
    Logger.processInputs("Drive/Gyro", gyroInputs);

    for (var module : modules) {
      module.periodic();
    }

    // Log empty setpoint states when disabled
    if (DriverStation.isDisabled()) {
      Logger.recordOutput("SwerveStates/Setpoints", new SwerveModuleState[] {});
      Logger.recordOutput("SwerveStates/SetpointsOptimized", new SwerveModuleState[] {});
    }

/*     Logger.recordOutput("Drive/Pose", odometry.getPoseMeters());

    odometry.update(getSwerveAngle(), getPositions());
 */  }

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
    return gyroInputs.yawPosition;
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
}
