// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package com.andromedalib.andromedaSwerve.andromedaModule;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class AndromedaModuleIOSim implements AndromedaModuleIO {
    private static final double LOOP_PERIOD_SECS = 0.02;
    private static final double WHEEL_RADIUS = Units.inchesToMeters(2.0);

    private DCMotorSim driveSim = new DCMotorSim(DCMotor.getNEO(1), 6.75, 0.025);
    private DCMotorSim turnSim = new DCMotorSim(DCMotor.getNEO(1), 150.0 / 7.0, 0.004);

    private final Rotation2d turnAbsoluteInitPosition = new Rotation2d(Math.random() * 2.0 * Math.PI);
    private double driveAppliedVolts = 0.0;
    private double turnAppliedVolts = 0.0;

    private final SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(0.0, 0.13);;
    private final PIDController driveFeedback = new PIDController(0.1, 0.0, 0.0);
    private final PIDController turnFeedback = new PIDController(10.0, 0.0, 0.0);

    @Override
    public void updateInputs(AndromedaModuleIOInputs inputs) {
        driveSim.update(LOOP_PERIOD_SECS);
        turnSim.update(LOOP_PERIOD_SECS);

        inputs.drivePosition = driveSim.getAngularPositionRad();
        inputs.driveVelocity = driveSim.getAngularVelocityRadPerSec();

        inputs.encoderAbsolutePosition = new Rotation2d(turnSim.getAngularPositionRad()).plus(turnAbsoluteInitPosition);
        inputs.steerAngle = new Rotation2d(turnSim.getAngularPositionRad());

        inputs.driveAppliedVolts = driveAppliedVolts;

        inputs.turnVelocity = turnSim.getAngularVelocityRadPerSec();
        inputs.turnAppliedVolts = turnAppliedVolts;
    }

    @Override
    public void setDriveSpeed(SwerveModuleState state) {
        double velocityRadPerSec = state.speedMetersPerSecond / WHEEL_RADIUS;

        double volt = driveFeedforward.calculate(velocityRadPerSec)
                + driveFeedback.calculate(driveSim.getAngularVelocityRadPerSec(), velocityRadPerSec);

        driveAppliedVolts = MathUtil.clamp(volt, -12.0, 12.0);
        driveSim.setInputVoltage(driveAppliedVolts);
    }

    @Override
    public void setTurnPosition(Rotation2d angle) {
        double volts = turnFeedback.calculate(turnSim.getAngularPositionRad(), angle.getRadians());
        turnAppliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
        turnSim.setInputVoltage(turnAppliedVolts);
    }
}
