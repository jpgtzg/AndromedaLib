/**
 * Written by Juan Pablo Gutiérrez
 */

package com.andromedalib.motorControllers;

import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * Wrapper for the {@link WPI_VictorSPX} class
 * That implements the HyperMotorController interface
 */
public class SuperVictor extends WPI_VictorSPX implements HyperMotorController {

    private double currentLimit = 1;

    ErrorCode error;

    /**
     * Configures SuperVictor motor controller
     * 
     * @param motorID    ID of the motor controller
     * @param idleMode   Idle mode of the motor controller
     * @param isInverted Inverted state of the motor controller
     */
    public SuperVictor(int motorID, GlobalIdleMode idleMode, boolean isInverted) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
    }

    @Override
    public void outputTelemetry(String tabName) {
        Shuffleboard.getTab(tabName).add("Victor Motor " + getBaseID() + "Voltage:", getBusVoltage());
        Shuffleboard.getTab(tabName).add("Victor Motor " + getBaseID() + "Temperature", getTemperature());
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
     * Gets the position of the motor controller
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

    @Override
    public void resetEncoder() {
        // TODO COMPLETE
    }

}
