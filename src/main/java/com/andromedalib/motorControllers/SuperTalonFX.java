package com.andromedalib.motorControllers;

import com.andromedalib.leds.Blinkin;
import com.andromedalib.math.Conversions;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Wrapper for the WPI_TalonFX class
 * That implements the HyperMotorController interface
 */
public class SuperTalonFX extends WPI_TalonFX implements HyperMotorController {

    private double currentLimit = 1;

    Blinkin blinkin;

    ErrorCode error;

    /**
     * Configures SuperTalonFX motor controller
     * 
     * @param motorID            ID of the motor controller
     * @param idleMode      Idle mode of the motor controller
     * @param inverted      Inverted state of the motor controller
     * @param configuration Stator current limit configuration
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, Boolean isInverted,
            StatorCurrentLimitConfiguration configuration) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        configStatorCurrentLimit(configuration);
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
     * Configures SuperTalonFX motor controller
     * 
     * @param motorID        ID of the motor controller
     * @param idleMode       Idle mode of the motor controller
     * @param isInverted     Inverted state of the motor controller
     * @param configuration  Stator current limit configuration
     * @param blinkinPWMPort PWM port of the blinkin
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, Boolean isInverted,
            StatorCurrentLimitConfiguration configuration,
            int blinkinPWMPort) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        configStatorCurrentLimit(configuration);
        blinkin = Blinkin.getInstance(blinkinPWMPort);
    }

    /**
     * Configures a SuperTalonFX with the {@link NeutralMode} as Cosat, and a new
     * {@link Blinkin} at PWM port 0
     * 
     * @param motorID
     * @param isInverted
     * @param configuration
     */
    public SuperTalonFX(int motorID, boolean isInverted, StatorCurrentLimitConfiguration configuration) {
        super(motorID);
        configFactoryDefault();
        setMode(GlobalIdleMode.Coast);
        setInverted(isInverted);
        configStatorCurrentLimit(configuration);
        blinkin = Blinkin.getInstance(0);
    }

    /**
     * Configures SuperTalonFX motor controller
     * 
     * @param motorID        ID of the motor controller
     * @param idleMode       Idle mode of the motor controller
     * @param isInverted     Inverted state of the motor controller
     * @param blinkinPWMPort PWM port of the blinkin
     */
    public SuperTalonFX(int motorID, GlobalIdleMode idleMode, Boolean isInverted,
            double blinkinPWMPort) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        blinkin = Blinkin.getInstance((int) blinkinPWMPort);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Voltage:", getBusVoltage());
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Temperature", getTemperature());
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
        return Conversions.MPSToMeterPerSecond(getSelectedSensorVelocity(), circumference, gearRatio);
    }

    /**
     * Resets the encoder of the motor controller
     */
    @Override
    public void resetEncoder() {
        setSelectedSensorPosition(0);
    }

    /**
     * Sets the LED to the {@link ErrorCode} error
     * in the motor controller. You must start running this method
     * in your Subsystem's periodic method.
     */
    @Override
    public void setErrorLED() {
        error = getLastError();
        if (error != ErrorCode.OK) {
            DriverStation.reportError("FalconFX Error: " + error + " , in motor " +
                    getDeviceID(), false);
            switch (error) {
                case CAN_MSG_STALE:

                    break;
                case CAN_TX_FULL:

                    break;
                case TxFailed:

                    break;

                case InvalidParamValue:

                    break;
                case CAN_INVALID_PARAM:

                    break;
                case RxTimeout:

                    break;
                case CAN_MSG_NOT_FOUND:

                    break;
                case TxTimeout:

                    break;
                case CAN_NO_MORE_TX_JOBS:

                    break;
                case UnexpectedArbId:

                    break;
                case CAN_NO_SESSIONS_AVAIL:

                    break;
                case BufferFull:

                    break;
                case CAN_OVERFLOW:

                    break;
                case SensorNotPresent:

                    break;
                case FirmwareTooOld:

                    break;
                default:
                    break;
            }
        }

    }
}