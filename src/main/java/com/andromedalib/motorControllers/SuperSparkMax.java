package com.andromedalib.motorControllers;

import com.andromedalib.leds.Blinkin;
import com.andromedalib.motorControllers.IdleManager.GlobalIdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Wrapper for the CANSparkMax class
 * That implements the HyperMotorController interface
 */
public class SuperSparkMax extends CANSparkMax implements HyperMotorController {

    Blinkin blinkin;

    private double currentLimit = 1;

    RelativeEncoder encoder = getEncoder();

    REVLibError error;

    /**
     * Configures SuperSparkMax motor controller
     * 
     * @param id           ID of the motor controller
     * @param type         Type of the motor controller
     * @param idleMode     Idle mode of the motor controller
     * @param inverted     Inverted state of the motor controller
     * @param currentLimit Current limit of the motor controller in amps
     */
    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode, boolean isInverted, int currentLimit) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
        setInverted(isInverted);
        setSmartCurrentLimit(currentLimit);
        outputTelemetry();
    }

    /**
     * Configures SuperSparkMax motor controller
     * 
     * @param id       ID of the motor controller
     * @param type     Type of the motor controller
     * @param idleMode Idle mode of the motor controller
     * @param inverted Inverted state of the motor controller
     */
    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode, boolean isInverted) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
        setInverted(isInverted);
        outputTelemetry();
    }

    /**
     * Configures SuperSparkMax motor controller
     * 
     * @param id             ID of the motor controller
     * @param type           Type of the motor controller
     * @param idleMode       Idle mode of the motor controller
     * @param inverted       Inverted state of the motor controller
     * @param blinkinPWMPort PWM port of the blinkin
     */
    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode, boolean isInverted,
            double blinkinPWMPort) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
        setInverted(isInverted);
        outputTelemetry();
        blinkin = new Blinkin((int) blinkinPWMPort);
    }

    /**
     * Configures SuperSparkMax motor controller
     * 
     * @param id             ID of the motor controller
     * @param type           Type of the motor controller
     * @param idleMode       Idle mode of the motor controller
     * @param inverted       Inverted state of the motor controller
     * @param currentLimit   Current limit of the motor controller in amps
     * @param blinkinPWMPort PWM port of the blinkin
     */
    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode, boolean isInverted, int currentLimit,
            int blinkinPWMPort) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
        setInverted(isInverted);
        setSmartCurrentLimit(currentLimit);
        outputTelemetry();
        blinkin = new Blinkin(blinkinPWMPort);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("NEO Motor " + getDeviceId() + "Voltage:", getBusVoltage());
        SmartDashboard.putNumber("NEO Motor " + getDeviceId() + "Temperature", getMotorTemperature());
    }

    /**
     * Sets the idle mode of the motor controller
     * 
     * @param idleMode Idle mode of the motor controller
     */
    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setIdleMode(IdleManager.neutralToIdle(idleMode));
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
     * Sets the position ƒconversion factor for the encoder
     * 
     * @param conversionFactor Conversion factor for the encoder
     */
    public void setPositionConversionFactor(double conversionFactor) {
        encoder.setPositionConversionFactor(conversionFactor);
    }

    /**
     * Sets the velocity conversion factor for the encoder
     * 
     * @param conversionFactor Conversion factor for the encoder
     */
    public void setVelocityConversionFactor(double conversionFactor) {
        encoder.setVelocityConversionFactor(conversionFactor);
    }

    /**
     * Gets the position of the encoder
     * 
     * @return the position of the encoder
     */
    public double getPosition() {
        return encoder.getPosition();
    }

    /**
     * Gets the velocity of the encoder
     * 
     * @return the velocity of the encoder
     */
    public double getVelocity() {
        return encoder.getVelocity();
    }

    /**
     * Sets the encoder position to 0
     * 
     */
    @Override
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    /**
     * Sets the LED to the {@link REVLibError} error
     * in the motor controller. You must start running this method 
     * in your Subsystem's periodic method. 
     */
    @Override
    public void setErrorLED() {
        error = getLastError();
        if (error != REVLibError.kOk) {
            DriverStation.reportError("SparkMax Error: " + error + " , in motor " + getDeviceId(), false);
            switch (error) {
                case kError:

                    break;
                case kTimeout:

                    break;
                case kNotImplemented:

                    break;

                case kHALError:

                    break;
                case kCantFindFirmware:

                    break;
                case kFirmwareTooOld:

                    break;
                case kFirmwareTooNew:

                    break;
                case kParamInvalidID:

                    break;
                case kParamMismatchType:

                    break;
                case kParamAccessMode:

                    break;
                case kParamInvalid:

                    break;
                case kParamNotImplementedDeprecated:

                    break;
                case kFollowConfigMismatch:

                    break;
                case kInvalid:

                    break;
                case kSetpointOutOfRange:

                    break;
                case kUnknown:

                    break;
                case kCANDisconnected:

                    break;
                case kDuplicateCANId:

                    break;
                case kInvalidCANId:

                    break;
                case kSparkMaxDataPortAlreadyConfiguredDifferently:

                    break;
                default:
                    break;
            }
        }
    }
}