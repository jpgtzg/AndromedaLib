/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModule;
import com.andromedalib.andromedaSwerve.andromedaModule.AndromedaModuleIO;
import com.andromedalib.andromedaSwerve.andromedaModule.GyroIO;
import com.andromedalib.andromedaSwerve.andromedaModule.GyroIOInputsAutoLogged;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig;
import com.andromedalib.andromedaSwerve.config.AndromedaSwerveConfig.Mode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AndromedaSwerve extends SubsystemBase {
  private static AndromedaSwerve instance;

  private AndromedaModule[] modules = new AndromedaModule[4];
  public AndromedaSwerveConfig andromedaProfile;

  private final GyroIO gyroIO;
  private final GyroIOInputsAutoLogged gyroInputs = new GyroIOInputsAutoLogged();

  private SwerveDriveOdometry odometry;

  /*
   * private SwerveDriveOdometry odometry;
   */
  private AndromedaSwerve(Mode mode, GyroIO gyro, AndromedaModuleIO[] modulesIO, AndromedaSwerveConfig profileConfig) {
    this.andromedaProfile = profileConfig;
    modules[0] = new AndromedaModule(0, "Front Right", andromedaProfile, mode, modulesIO[0]);
    modules[1] = new AndromedaModule(1, "Back Right", andromedaProfile, mode, modulesIO[1]);
    modules[2] = new AndromedaModule(2, "Back Left", andromedaProfile, mode, modulesIO[2]);
    modules[3] = new AndromedaModule(3, "Front Left", andromedaProfile, mode, modulesIO[3]);

    this.gyroIO = gyro;

    odometry = new SwerveDriveOdometry(profileConfig.swerveKinematics, getSwerveAngle(), getPositions());

    /*
     * odometry = new SwerveDriveOdometry(profileConfig.swerveKinematics,
     * getSwerveAngle(), getPositions());
     */ }

  public static AndromedaSwerve getInstance(Mode mode, GyroIO gyro, AndromedaModuleIO[] modules,
      AndromedaSwerveConfig profileConfig) {
    if (instance == null) {
      instance = new AndromedaSwerve(mode, gyro, modules, profileConfig);
    }
    return instance;
  }

  @Override
  public void periodic() {
    gyroIO.updateInputs(gyroInputs);
    Logger.processInputs("Swerve/Gyro", gyroInputs);

    for (var module : modules) {
      module.periodic();
    }

    // Log empty setpoint states when disabled
    if (DriverStation.isDisabled()) {
      Logger.recordOutput("SwerveStates/Setpoints", new SwerveModuleState[] {});
      Logger.recordOutput("SwerveStates/SetpointsOptimized", new SwerveModuleState[] {});
    }
    Logger.recordOutput("SwerveStates/Measured", getModuleStates());

    odometry.update(getSwerveAngle(), getPositions());
  }

  /**
   * Main driving method
   * 
   * @param translation   Translation movement
   * @param rotation      Desired rotation
   * @param fieldRelative True if field relative
   * @param isOpenLoop    True if open loop
   */
  public void drive(Translation2d translation, double rotation, boolean fieldRelative) {
    SwerveModuleState[] swerveModuleStates = andromedaProfile.swerveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                getSwerveAngle())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));

    setModuleStates(swerveModuleStates);
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
   * Returns the module states (turn angles and drive velocities) for all of the
   * modules.
   */
  @AutoLogOutput(key = "SwerveStates/Measured")
  private SwerveModuleState[] getModuleStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (int i = 0; i < 4; i++) {
      states[i] = modules[i].getState();
    }
    return states;
  }

  /** Returns the current odometry pose. */
  @AutoLogOutput(key = "Odometry/Robot")
  public Pose2d getPose() {
    return odometry.getPoseMeters();
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
   * Sets the modules states
   * 
   * @param desiredStates Desired SwerveModuleState array
   * @param isOpenLoop    True if open loop driving
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, andromedaProfile.maxSpeed);

    Logger.recordOutput("SwerveStates/Setpoints", desiredStates);

    for (AndromedaModule andromedaModule : modules) {
      andromedaModule.setDesiredState(desiredStates[andromedaModule.getModuleNumber()]);
    }
  }
}
