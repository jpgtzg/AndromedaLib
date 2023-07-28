/**
 * Written by Juan Pablo Gutiérrez
 */
package com.andromedalib.andromedaSwerve.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.andromedalib.math.Functions;
import com.andromedalib.andromedaSwerve.systems.AndromedaSwerve;
import com.andromedalib.andromedaSwerve.utils.SwerveConstants;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SwerveDriveCommand extends CommandBase {

  AndromedaSwerve swerve;
  DoubleSupplier translationY, translationX, rotation;
  BooleanSupplier fieldOriented;

  SlewRateLimiter xLimiter, yLimiter, turningLimiter;

  /** Creates a new SwerveDrive. */
  public SwerveDriveCommand(AndromedaSwerve andromedaSwerve, DoubleSupplier translationX, DoubleSupplier translationY,
      DoubleSupplier rotation, BooleanSupplier fieldOrientedControl) {
    this.swerve = andromedaSwerve;
    this.translationY = translationY;
    this.translationX = translationX;
    this.rotation = rotation;
    this.fieldOriented = fieldOrientedControl;

    xLimiter = new SlewRateLimiter(SwerveConstants.maxAcceleration);
    yLimiter = new SlewRateLimiter(SwerveConstants.maxAcceleration);
    turningLimiter = new SlewRateLimiter(SwerveConstants.maxAngularAcceleration);

    addRequirements(swerve);
  }

  @Override
  public void execute() {
    double translationYVal = Functions.handleDeadband(translationY.getAsDouble(), SwerveConstants.deadband);
    double translationXVal = Functions.handleDeadband(translationX.getAsDouble(), SwerveConstants.deadband);
    double rotationVal = Functions.handleDeadband(rotation.getAsDouble(), SwerveConstants.deadband);

    double ySpeed = yLimiter.calculate(translationYVal);
    double xSpeed = xLimiter.calculate(translationXVal);
    double rotationSpeed = turningLimiter.calculate(rotationVal);

    swerve.drive(
        new Translation2d(ySpeed, xSpeed).times(SwerveConstants.maxSpeed),
        rotationSpeed * SwerveConstants.maxAngularVelocity,
        !fieldOriented.getAsBoolean(), 
        true);
  }

}
