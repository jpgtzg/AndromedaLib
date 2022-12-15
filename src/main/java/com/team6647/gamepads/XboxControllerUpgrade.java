package com.team6647.gamepads;

import com.team6647.gamepads.AxisButton.*;

import edu.wpi.first.wpilibj.XboxController;

/**
 * XboxController wrapper that extends the WPI XboxController class
 * 
 * Copied over and modified from
 * https://github.com/Spectrum3847/SpectrumLib/blob/main/src/main/java/frc/SpectrumLib/gamepads/XboxGamepad.java
 */
public class XboxControllerUpgrade extends XboxController{
    private double leftThreshold = 0.05;
    private double rightThreshold = 0.05;

    public XboxControllerUpgrade (int port) {
       
        super(port);
    }
    public XboxControllerUpgrade (int port, double leftThreshold, double rightThreshold) {
        this(port);
        this.leftThreshold = leftThreshold;
        this.rightThreshold = rightThreshold;
    }
    public XboxControllerUpgrade (int port, double triggersThreshold) {
        this(port, triggersThreshold, triggersThreshold);
    }
	public AxisButton leftTriggerButton = new AxisButton(this, Axis.kLeftTrigger, leftThreshold, ThresholdType.GREATER_THAN);
	public AxisButton rightTriggerButton = new AxisButton(this, Axis.kRightTrigger, rightThreshold, ThresholdType.GREATER_THAN);
	public Dpad Dpad = new Dpad(this);
    
    public void setTriggerThreshold(Axis axis,double threshold){
        if(axis ==Axis.kLeftTrigger){
            leftThreshold = threshold;
        }else if(axis ==Axis.kRightTrigger){
            rightThreshold = threshold;
        }
    }

	public void setRumble(double leftValue, double rightValue) {
		setRumble(RumbleType.kLeftRumble, leftValue);
		setRumble(RumbleType.kRightRumble, rightValue);
	}

	public static enum XboxDpad {
		UNPRESSED(-1), UP(0), UP_RIGHT(45), RIGHT(90), DOWN_RIGHT(135), DOWN(180), DOWN_LEFT(225), LEFT(270),
		UP_LEFT(315);

		final int value;

		XboxDpad(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

    public class Dpad {
        private final byte dpad = 6;
        public final XboxControllerUpgrade joy;
        public AxisButton Up;
        public AxisButton Down;
        public AxisButton Left;
        public AxisButton Right;
        public AxisButton UpLeft;
        public AxisButton UpRight;
        public AxisButton DownLeft;
        public AxisButton DownRight;
        
        public Dpad(XboxControllerUpgrade xboxControllerUpgrade) {
            this.joy = xboxControllerUpgrade;
            this.Up = new AxisButton(joy, dpad, XboxDpad.UP.value, ThresholdType.POV);
            this.Down = new AxisButton(joy, dpad, XboxDpad.DOWN.value, ThresholdType.POV);		
            this.Left = new AxisButton(joy, dpad, XboxDpad.LEFT.value, ThresholdType.POV);
            this.Right = new AxisButton(joy, dpad, XboxDpad.RIGHT.value, ThresholdType.POV);
            this.UpLeft = new AxisButton(joy, dpad, XboxDpad.UP_LEFT.value, ThresholdType.POV);
            this.UpRight = new AxisButton(joy, dpad, XboxDpad.UP_RIGHT.value, ThresholdType.POV);
            this.DownLeft = new AxisButton(joy, dpad, XboxDpad.DOWN_LEFT.value, ThresholdType.POV);
            this.DownRight = new AxisButton(joy, dpad, XboxDpad.DOWN_RIGHT.value, ThresholdType.POV);
        }
        
        public double getValue() {
            return joy.getRawAxis(dpad);
        }
    }

}




