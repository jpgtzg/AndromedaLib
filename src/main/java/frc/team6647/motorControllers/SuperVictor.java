package frc.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.team6647.motorControllers.IdleManager.GlobalIdleMode;

public class SuperVictor extends WPI_VictorSPX implements SuperMotorController {

    private double currentLimit = 1;

    /**
     * Configures SuperVictor motor controller
     * 
     * @param id           - ID of the motor controller
     * @param type         - Type of the motor controller
     * @param idleMode     - Idle mode of the motor controller
     * @param inverted     - Inverted state of the motor controller
     * @param currentLimit - Current limit of the motor controller in amps
     */
    public SuperVictor(int motorID, GlobalIdleMode idleMode, boolean isInverted, int currentLimit) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
        setInverted(isInverted);
        // TODO SET CURRENT LIMIT
    }

    @Override
    public void setMode(GlobalIdleMode idleMode) {
        setNeutralMode(IdleManager.idleToNeutral(idleMode));
    }

    @Override
    public double getLimit() {
        return currentLimit;
    }

    public void setLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }

}
