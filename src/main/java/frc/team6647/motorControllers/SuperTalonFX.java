package frc.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.team6647.motorControllers.IdleManager.GlobalIdleMode;

public class SuperTalonFX extends WPI_TalonFX{

    public SuperTalonFX(int motorID, GlobalIdleMode idleMode) {
        super(motorID);
        configFactoryDefault();
    }
    
    
}
