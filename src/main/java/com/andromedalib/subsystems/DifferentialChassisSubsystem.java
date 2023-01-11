// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.andromedalib.subsystems;

import com.andromedalib.motorControllers.SuperTalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DifferentialChassisSubsystem extends SubsystemBase {
  MotorControllerGroup leftMotorController;
  MotorControllerGroup rightMotorController;

  double leftSpeed;
  double rightSpeed;

  DifferentialDrive drive;

  public DifferentialChassisSubsystem(SuperTalonFX[] leftMotors, SuperTalonFX[] rightMotors) {
    leftMotorController = new MotorControllerGroup(leftMotors);
    rightMotorController = new MotorControllerGroup(rightMotors);

    drive = new DifferentialDrive(leftMotorController, rightMotorController);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left Drive Speed", leftSpeed);
    SmartDashboard.putNumber("Right Drive Speed", rightSpeed);

  }

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

  public void curvatureDrive(double rightSpeed, double leftSpeed) {
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
    drive.curvatureDrive(leftSpeed, rightSpeed, true);
  }
}
