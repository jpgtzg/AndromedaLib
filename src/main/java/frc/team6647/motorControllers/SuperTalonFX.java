package frc.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team6647.motorControllers.IdleManager.GlobalIdleMode;

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
        // TODo SET CURRENT LIMIT
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Voltage:", getBusVoltage());
        SmartDashboard.putNumber("TalonFX Motor " + getBaseID() + "Temperature", getTemperature());
    }

    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setNeutralMode(IdleManager.idleToNeutral(idleMode));
    }

    @Override
    public double getLimit() {
        return currentLimit;
    }

    @Override
    public void setLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }

    /**
     * Gets the position of the motor controller
     * 
     * @param conversionFactor - Conversion factor to convert encoder ticks to
     *                         desired units
     * 
     * @return the position of the motor in desired units
     */
    public double getPosition(double conversionFactor) {
        return getSelectedSensorPosition() * conversionFactor;
    }

    /**
     * Gets the velocity of the motor controller
     * 
     * @param conversionFactor - Conversion factor to convert encoder ticks to
     *                         desired units
     * 
     * @return the velocity of the motor deisred units
     */
    public double getVelocity(double conversionFactor) {
        return getSelectedSensorVelocity() * conversionFactor;
    }
}
