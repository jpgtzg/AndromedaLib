/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.motorControllers;

import com.andromedalib.leds.Blinkin;
import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * Wrapper for the WPI_TalonFX class
 * That implements the HyperMotorController interface
 */
public class SuperTalonFX extends WPI_TalonFX implements HyperMotorController {

    private double currentLimit = 1;

    ErrorCode error;

    /**
     * Configures SuperTalonFX motor controller
     * 
     * @param motorID       ID of the motor controller
     * @param idleMode      Idle mode of the motor controller
     * @param inverted      Inverted state of the motor controller
     * @param configuration Stator current limit configuration
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, Boolean isInverted,
            TalonFXConfiguration configuration) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        configAllSettings(configuration);
    }

    /**
     * Configures SuperTalonFX motor controller
     * 
     * @param motorID    ID of the motor controller
     * @param idleMode   Idle mode of the motor controller
     * @param isInverted Inverted state of the motor controller
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, Boolean isInverted) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
    }

    /**
     * Configures a SuperTalonFX with the {@link NeutralMode} as Coast, and a new
     * {@link Blinkin} at PWM port 0
     * 
     * @param motorID
     * @param isInverted
     * @param configuration
     */
    public SuperTalonFX(int motorID, boolean isInverted, TalonFXConfiguration configuration) {
        super(motorID);
        configFactoryDefault();
        setMode(GlobalIdleMode.Coast);
        setInverted(isInverted);
        configAllSettings(configuration);
    }

    /**
     * Configures a SuperTalonFX with the {@link NeutralMode} as Coast, a new
     * {@link Blinkin} at PWM port 0, and without an inversion
     * 
     * @param motorID
     * @param isInverted
     * @param configuration
     */
    public SuperTalonFX(int motorID, TalonFXConfiguration configuration) {
        super(motorID);
        configFactoryDefault();
        setMode(GlobalIdleMode.Coast);
        configAllSettings(configuration);
    }

    @Override
    public void outputTelemetry(String tabName) {
        Shuffleboard.getTab(tabName).add("TalonFX Motor " + getBaseID() + "Voltage:", getBusVoltage());
        Shuffleboard.getTab(tabName).add("TalonFX Motor " + getBaseID() + "Temperature", getTemperature());
    }

    /**
     * Sets the idle mode of the motor controller
     * 
     * @param idleMode Idle mode of the motor controller
     */
    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setNeutralMode(IdleManager.idleToNeutral(idleMode));
    }

    /**
     * Sets the current limit of the motor controller
     * 
     * @param currentLimit Current limit of the motor controller in amps
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
     * Gets the position of the motor controller adjusted to a circumference (Useful
     * for tank drives)
     * 
     * @param circumference Circumference of the wheel in meters
     * @param gearRatio     Gear ratio of the motor controller
     * 
     * @return the position of the motor in meters
     */
    public double getPosition(double circumference, double gearRatio) {
        return Conversions.falconToMeters(getSelectedSensorPosition(), circumference, gearRatio);
    }

    /**
     * Gets the motor angle adjusted to the gear ratio
     * 
     * @param gearRatio Gear ratio of the mechanism
     * 
     * @return the angle of the motor in degrees
     */
    public double getAngle(double gearRatio) {
        return Conversions.falconToDegrees(getSelectedSensorPosition(), gearRatio);
    }

    /**
     * Gets the velocity of the motor controller
     * 
     * @param circumference Circumference of the wheel in meters
     * @param gearRatio     Gear ratio of the motor controller
     * 
     * @return the velocity of the motor in meters per second
     */
    public double getVelocity(double circumference, double gearRatio) {
        return Conversions.falconToMPS(getSelectedSensorVelocity(), circumference, gearRatio);
    }

    /**
     * Resets the encoder of the motor controller
     */
    @Override
    public void resetEncoder() {
        setSelectedSensorPosition(0);
    }

}