package com.arduino.simulator.model;

import java.util.Random;

public class Sensor {
    private final int id;
    private final String name;
    private final SensorType type;
    private final int pos;
    private final Random random = new Random();
    private Double manualValue = null; // Valeur manuelle définie par l'utilisateur

    public Sensor(int id, String name, SensorType type, int pos) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return type.getUnit();
    }

    public int getPos() {
        return pos;
    }

    /**
     * Définit une valeur manuelle pour le capteur
     */
    public void setValue(double value) {
        this.manualValue = value;
    }

    /**
     * Réinitialise le capteur en mode aléatoire
     */
    public void resetToRandom() {
        this.manualValue = null;
    }

    /**
     * Simule la lecture du capteur avec des valeurs aléatoires réalistes
     */
    public double readValue() {
        // Si une valeur manuelle est définie, la retourner
        if (manualValue != null) {
            return manualValue;
        }

        switch (type) {
            case TEMPERATURE:
                // Température entre 15 et 30°C
                return 15 + random.nextDouble() * 15;
            case LIGHT:
                // Luminosité entre 0 et 1000 lux
                return random.nextDouble() * 1000;
            default:
                return 0;
        }
    }
}
