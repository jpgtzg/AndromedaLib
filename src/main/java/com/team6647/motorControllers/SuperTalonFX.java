package com.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team6647.math.Conversions;
import com.team6647.motorControllers.IdleManager.GlobalIdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SuperTalonFX extends WPI_TalonFX implements HyperMotorController {

    private double currentLimit = 1;

    /**
     * Configures SuperVictor motor controller
     * 
     * @param id           - ID of the motor controller
     * @param idleMode     - Idle mode of the motor controller
     * @param inverted     - Inverted state of the motor controller
     * @param currentLimit - Current limit of the motor controller in amps
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, boolean isInverted, int currentLimit) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        // TODO SET CURRENT LIMIT
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Voltage:", getBusVoltage());
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Temperature", getTemperature());
    }

    /**
     * Sets the idle mode of the motor controller
     * 
     * @param idleMode - Idle mode of the motor controller
     */
    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setNeutralMode(IdleManager.idleToNeutral(idleMode));
    }

    /**
     * Sets the current limit of the motor controller
     * 
     * @param currentLimit - Current limit of the motor controller in amps
     */
    @Override
    public void setLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }

    /**
     * Gets the current limit of the motor controller
     * 
     * @return the current limit of the motor controller
     */
    @Override
    public double getLimit() {
        return currentLimit;
    }

    /**
     * Gets the position of the motor controller
     * 
     * @param circumference - Circumference of the wheel in meters
     * @param gearRatio     - Gear ratio of the motor controller
     * 
     * @return the position of the motor in meters
     */
    public double getPosition(double circumference, double gearRatio) {
        return Conversions.falconToMeters(getSelectedSensorPosition(), circumference, gearRatio);
    }

    /**
     * Gets the velocity of the motor controller
     * 
     * @param circumference - Circumference of the wheel in meters
     * @param gearRatio     - Gear ratio of the motor controller
     * 
     * @return the velocity of the motor in meters per second
     */
    public double getVelocity(double circumference, double gearRatio) {
        return Conversions.MPSToMeterPerSecond(getSelectedSensorVelocity(), circumference, gearRatio);
    }

    /**
     * Resets the encoder of the motor controller
     */
    @Override
    public void resetEncoder() {
        setSelectedSensorPosition(0);
    }
}
