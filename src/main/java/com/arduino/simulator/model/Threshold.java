package com.arduino.simulator.model;

public class Threshold {
    private int sensor;
    private double val;
    private int mode; // 0 = greater than, 1 = lesser than, 2 = switch
    private Double previousValue = null; // Pour le mode switch

    public Threshold(int sensor, double val, int mode) {
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

    /**
     * Vérifie si le seuil est dépassé
     * @param sensorValue la valeur actuelle du capteur
     * @return true si le seuil est dépassé
     */
    public boolean isTriggered(double sensorValue) {
        boolean triggered = false;

        if (mode == 0) {
            // Mode "greater than" - déclenche si la valeur est au dessus
            triggered = sensorValue > val;
        } else if (mode == 1) {
            // Mode "lesser than" - déclenche si la valeur est en dessous
            triggered = sensorValue < val;
        } else if (mode == 2) {
            // Mode "switch" - déclenche quand la valeur traverse le seuil
            if (previousValue != null) {
                // Vérifie si l'ancienne et la nouvelle valeur sont de côtés différents du seuil
                boolean wasAbove = previousValue > val;
                boolean isAbove = sensorValue > val;
                triggered = wasAbove != isAbove; // Déclenche si changement de côté
            }
            previousValue = sensorValue;
        }

        return triggered;
    }

    /**
     * Réinitialise la valeur précédente (utile pour le mode switch)
     */
    public void reset() {
        previousValue = null;
    }
}
