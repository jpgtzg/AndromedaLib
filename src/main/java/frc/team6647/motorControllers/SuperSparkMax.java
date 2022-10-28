package frc.team6647.motorControllers;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team6647.motorControllers.IdleManager.GlobalIdleMode;

public class SuperSparkMax extends CANSparkMax implements SuperMotorController{

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
        sisData();
    }


    public void sisData() {
        SmartDashboard.putNumber("Voltage:", getBusVoltage());
    }

    @Override
    public double getLimit() {
        return currentLimit;
    }

    @Override
    public void setLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }


    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setIdleMode(IdleManager.neutralToIdle(idleMode));
        
    }
}