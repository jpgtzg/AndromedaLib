package frc.team6647.motorControllers;
import com.revrobotics.CANSparkMax;

import frc.team6647.motorControllers.SuperIdleMode.GlobalIdleMode;

public class SuperSparkMax extends CANSparkMax {

    public SuperSparkMax(int motorID, MotorType type, GlobalIdleMode idleMode) {
        super(motorID, type);
        restoreFactoryDefaults();
        setMode(idleMode);
    }

    public void setMode(GlobalIdleMode idleMode) {
        setIdleMode(SuperIdleMode.neutralToIdle(idleMode));
    }
}