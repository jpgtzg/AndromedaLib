// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.andromedalib.subsystems;

import com.andromedalib.motorControllers.SuperTalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DifferentialDriveSubsystem extends SubsystemBase {
  private MotorControllerGroup leftMotorController;
  private MotorControllerGroup rightMotorController;

  private double leftSpeed;
  private double rightSpeed;

  private DifferentialDrive drive;

  public boolean driveInverted = false;

  public DifferentialDriveSubsystem(SuperTalonFX[] leftMotors, SuperTalonFX[] rightMotors) {
    leftMotorController = new MotorControllerGroup(leftMotors);
    rightMotorController = new MotorControllerGroup(rightMotors);
    drive = new DifferentialDrive(leftMotorController, rightMotorController);
  }

  /**
   * Publishes data and information to SmartDashboard
   */
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left Drive Speed", leftSpeed);
    SmartDashboard.putNumber("Right Drive Speed", rightSpeed);
    SmartDashboard.putData(drive);
    outputTelemetry();
  }

  /**
   * Publishes data and information to SmartDashboard. Override this method to
   * include custom information
   */
  public void outputTelemetry() {}

  /**
   * Tank drive
   * 
   * @param leftSpeed  Left side speed
   * @param rightSpeed Right side speed
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
    drive.tankDrive(this.leftSpeed, this.rightSpeed);
  }

  /**
   * Arcade drive
   * 
   * @param linearSpeed X speed
   * @param rotSpeed    Rotational value
   */
  public void arcadeDrive(double linearSpeed, double rotSpeed) {
    this.leftSpeed = linearSpeed;
    this.rightSpeed = rotSpeed;
    drive.arcadeDrive(leftSpeed, rightSpeed);
  }

  /**
   * Curvature Drive. Allows turn in place
   * 
   * @param linearSpeed X speed
   * @param rotSpeed    Rotational value
   */
  public void curvatureDrive(double linearSpeed, double rotSpeed) {
    this.leftSpeed = linearSpeed;
    this.rightSpeed = rotSpeed;
    drive.curvatureDrive(leftSpeed, rightSpeed, true);
  }

  /**
   * Returns whether the drive is inverted or not. Useful for the tank drive
   * inverted commands
   * 
   * @return Current inverted state
   */
  public boolean isInverted() {
    return driveInverted;
  }
}