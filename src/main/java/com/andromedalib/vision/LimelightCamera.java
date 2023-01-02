package com.andromedalib.vision;

public class LimelightCamera {

    private final LimelightLowLevel lTable;

    /**
     * Create a new LimelightCamera object, with the default name of "limelight"
     */
    public LimelightCamera() {
        lTable = new LimelightLowLevel("limelight");
    }

    /**
     * Create a new LimelightCamera object
     * 
     * @param name Name of the Limelight Network Table
     */
    public LimelightCamera(String name) {
        lTable = new LimelightLowLevel(name);
    }

    /**
     * Checks if the Limelight is connected
     * 
     * @return True if the Limelight is connected, false otherwise
     */
    public boolean isConnected() {
        return lTable.tableInstance.isConnected();
    }

    /**
     * Get the current pipeline number
     * 
     * @return Current pipeline number
     */
    public double getPipeline() {
        return lTable.pipelineIndex.getDouble(0);
    }

    /**
     * Set the current pipeline number
     * 
     * @param pipeline Pipeline number to set
     */
    public void setPipeline(double pipeline) {
        lTable.setPipeline.setDouble(pipeline);
    }

}
