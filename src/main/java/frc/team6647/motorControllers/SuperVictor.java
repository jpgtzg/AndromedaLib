package frc.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.team6647.motorControllers.SuperIdleMode.GlobalIdleMode;

public class SuperVictor extends WPI_VictorSPX{

    public SuperVictor(int motorID, GlobalIdleMode idleMode) {
        super(motorID);
        configFactoryDefault();
        setMode(idleMode);
    }

    public void setMode(GlobalIdleMode idleMode) {
        setNeutralMode(SuperIdleMode.idleToNeutral(idleMode));  
    }
}
