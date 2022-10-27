package frc.team6647.motorControllers;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;
/*
 * Global class for SuperMotorController idle modes
 */
public class SuperIdleMode {
    public enum GlobalIdleMode {
        Coast, brake
    }

    /* Converts from GlobalIdle mode to corresponding NeutralMode */
    static NeutralMode idleToNeutral(GlobalIdleMode idleMode) {
        return NeutralMode.values()[idleMode.ordinal() + 1];
    }

    /* Converts from GlobalIdle mode to corresponding IdleMode */
    static IdleMode neutralToIdle(GlobalIdleMode idleMode) {
        return IdleMode.values()[idleMode.ordinal()];
    }
}
