// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team6647.odometry;

import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


//TODO: Not ready for use
/**
 * Class used to calculate the robot's position on the field
 * through the use of an unfiltered Kalman filter in the
 * {@link DifferentialDrivePoseEstimator} class
 * 
 */
public class DifferentialRobotPose extends SubsystemBase {
  static DifferentialDrivePoseEstimator poseEstimator;

  static Field2d field2d;

  /**
   * Creates a new DifferentialRobotPose.
   */
  public DifferentialRobotPose() {}

  @Override
  public void periodic() {
    field2d.setRobotPose(getEstimatedPosition());
  }

  public void outputTelemetry() {
    SmartDashboard.putData(field2d);
  }

  /**
   * Gets the estimated position of the robot
   * 
   * @return the estimated position of the robot
   */
  public Pose2d getEstimatedPosition() {
    return poseEstimator.getEstimatedPosition();
  }

  /**
   * Gets the pose estimator
   * 
   * @return the pose estimator
   */
  public DifferentialDrivePoseEstimator getPoseEstimator() {
    return poseEstimator;
  }

  public Field2d getField2d() {
    return field2d;
  }

  public void updatePoseEstimator(Rotation2d gyroAngle, DifferentialDriveWheelSpeeds wheelSpeeds, double leftDistance,
      double rightDistance) {

    poseEstimator.update(gyroAngle, wheelSpeeds, leftDistance, rightDistance);

  }

}
