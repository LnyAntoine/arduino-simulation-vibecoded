package com.arduino.simulator.service;

import com.arduino.simulator.model.Sensor;
import com.arduino.simulator.model.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorService {
    private final Map<Integer, Sensor> sensors = new HashMap<>();

    private final LedService ledService;

    @Autowired
    public SensorService(@Lazy LedService ledService) {
        this.ledService = ledService;
        // Initialisation des capteurs (équivalent à SensorManager)
        sensors.put(0, new Sensor(0, "Light Sensor 1", SensorType.LIGHT, 2));
        sensors.put(1, new Sensor(1, "Temperature Sensor 1", SensorType.TEMPERATURE, 1));
    }

    public Sensor getSensorById(int id) {
        return sensors.get(id);
    }

    public List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }

    public List<Sensor> getSensorsByIds(List<Integer> ids) {
        List<Sensor> result = new ArrayList<>();
        for (Integer id : ids) {
            Sensor sensor = sensors.get(id);
            if (sensor != null) {
                result.add(sensor);
            }
        }
        return result;
    }

    /**
     * Met à jour la valeur d'un capteur
     */
    public boolean updateSensorValue(int id, double value) {
        Sensor sensor = sensors.get(id);
        if (sensor != null) {
            sensor.setValue(value);
            // Vérifier le threshold après la mise à jour
            ledService.checkThreshold();
            return true;
        }
        return false;
    }

    /**
     * Réinitialise un capteur en mode aléatoire
     */
    public boolean resetSensor(int id) {
        Sensor sensor = sensors.get(id);
        if (sensor != null) {
            sensor.resetToRandom();
            // Vérifier le threshold après la réinitialisation
            ledService.checkThreshold();
            return true;
        }
        return false;
    }
}
