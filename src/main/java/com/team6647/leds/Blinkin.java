// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team6647.leds;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Wrapper for the Blinkin LED controller
 */
public class Blinkin extends SubsystemBase {

  public static Spark blinkin;

  /**
   * Setsup the Blinkin LED controller
   */
  public Blinkin(int pwmPort) {
    blinkin = new Spark(pwmPort);
    solidBlack();
  }

  /**
   * Sets the LED to fire
   */
  public void fire() {
    blinkin.set(-0.59);
  }

  /*
   * Sets the LED to solid black
   */
  public void solidBlack() {
    blinkin.set(-0.73);

  }

  /*
   * Sets the LED to breathe blue
   */
  public void breathBlue() {
    blinkin.set(-0.15);

  }

  /*
   * Sets the LED to dark green
   */
  public void darkGreen() {
    blinkin.set(0.75);
  }

  /*
   * Sets the LED to hearbeat fast
   */
  public void Heartbeatfast() {
    blinkin.set(0.07);
  }

  /*
   * Sets the LED to light chase blue
   */
  public void LightChaseBlue() {
    blinkin.set(-0.29);
  }

  /*
   * Sets the LED to larson scanner red
   */
  public void larsonScannerRed() {
    blinkin.set(-0.35);
  }

  /*
   * Sets the LED to strobe gold
   */
  public void strobeGold() {
    blinkin.set(-0.07);
  }

  /*
   * Sets the LED to the alliance color
   */
  public void allianceColor() {
    boolean isRed = NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);
    if (isRed == true) {
      blinkin.set(-0.39);
      System.out.println("Color Waves, Lava Palette");
    } else {
      blinkin.set(-0.23);
      System.out.println("Heartbeat, Blue");
    }
  }
}
