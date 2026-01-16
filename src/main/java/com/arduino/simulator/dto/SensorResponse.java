package com.arduino.simulator.dto;

public class SensorResponse {
    private int id;
    private String name;
    private double val;
    private String unit;
    private int pos;

    public SensorResponse(int id, String name, double val, String unit, int pos) {
        this.id = id;
        this.name = name;
        this.val = val;
        this.unit = unit;
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
