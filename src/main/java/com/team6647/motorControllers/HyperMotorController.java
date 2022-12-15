package com.team6647.motorControllers;

import com.team6647.motorControllers.IdleManager.GlobalIdleMode;

/** 
 * Interface to implement the same functions and
 * variables on all motor controllers
 */
interface HyperMotorController {
    
    public void setMode(GlobalIdleMode idleMode);

    public double getLimit();

    public void setLimit(int currentLimit);

    /*
     * Outputs Telemetry to SmartDashboard
     */
    public void outputTelemetry();

    public void resetEncoder();
}
