package com.team6647.motorControllers;

import com.revrobotics.CANSparkMax;
import com.team6647.motorControllers.IdleManager.GlobalIdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SuperSparkMax extends CANSparkMax implements HyperMotorController {

    private double currentLimit = 1;

    /**
     * Configures SuperSparkMax motor controller
     * 
     * @param id           - ID of the motor controller
     * @param type         - Type of the motor controller
     * @param idleMode     - Idle mode of the motor controller
     * @param inverted     - Inverted state of the motor controller
     * @param currentLimit - Current limit of the motor controller in amps
     */
    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode, boolean isInverted, int currentLimit) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
        setInverted(isInverted);
        setSmartCurrentLimit(currentLimit);
        outputTelemetry();
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("NEO Motor " + getDeviceId() + "Voltage:", getBusVoltage());
        SmartDashboard.putNumber("NEO Motor " + getDeviceId() + "Temperature", getMotorTemperature());
    }

    /**
     * Sets the idle mode of the motor controller
     * 
     * @param idleMode - Idle mode of the motor controller
     */
    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setIdleMode(IdleManager.neutralToIdle(idleMode));
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

}