package frc.team6647.motorControllers;

import frc.team6647.motorControllers.IdleManager.GlobalIdleMode;

interface SuperMotorController {
    
    double currentLimit = 1;

    public void setMode(GlobalIdleMode idleMode);

    public double getLimit();

    public void setLimit(int currentLimit);
}
