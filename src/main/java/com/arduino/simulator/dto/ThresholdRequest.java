package com.arduino.simulator.dto;

public class ThresholdRequest {
    private int sensor;
    private double val;
    private int mode;

    public ThresholdRequest() {
    }

    public ThresholdRequest(int sensor, double val, int mode) {
        this.sensor = sensor;
        this.val = val;
        this.mode = mode;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
