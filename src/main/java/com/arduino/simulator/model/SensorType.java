package com.arduino.simulator.model;

public enum SensorType {
    TEMPERATURE("Temperature", "°C"),
    LIGHT("Light", "lux");

    private final String name;
    private final String unit;

    SensorType(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }
}
