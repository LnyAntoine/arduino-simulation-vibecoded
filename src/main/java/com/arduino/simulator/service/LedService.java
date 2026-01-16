package com.arduino.simulator.service;

import com.arduino.simulator.model.Sensor;
import com.arduino.simulator.model.Threshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedService {
    private int status = 0; // 0 = éteinte, 1 = allumée
    private final int ledPos = 13; // Position de la LED (typique pour Arduino)
    private Threshold threshold = null;

    @Autowired
    private SensorService sensorService;

    public void ledOn() {
        status = 1;
        System.out.println("LED allumée");
    }

    public void ledOff() {
        status = 0;
        System.out.println("LED éteinte");
    }

    public void toggle() {
        status = (status == 0) ? 1 : 0;
        System.out.println("LED toggle: " + (status == 1 ? "allumée" : "éteinte"));
    }

    public int getStatus() {
        return status;
    }

    public int getLedPos() {
        return ledPos;
    }

    public void setSensorThreshold(int sensorId, double val, int mode) {
        threshold = new Threshold(sensorId, val, mode);
        System.out.println("Threshold configuré: sensor=" + sensorId + ", val=" + val + ", mode=" + mode);
        checkThreshold();
    }

    public void deleteThreshold() {
        threshold = null;
        System.out.println("Threshold supprimé");
    }

    public Threshold getThreshold() {
        return threshold;
    }

    /**
     * Vérifie le seuil et allume/éteint la LED en conséquence
     */
    public void checkThreshold() {
        if (threshold == null) {
            return;
        }

        Sensor sensor = sensorService.getSensorById(threshold.getSensor());
        if (sensor == null) {
            return;
        }

        double sensorValue = sensor.readValue();
        if (threshold.isTriggered(sensorValue)) {
            ledOn();
        } else {
            ledOff();
        }
    }
}
