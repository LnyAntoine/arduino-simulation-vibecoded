package com.arduino.simulator.controller;

import com.arduino.simulator.dto.SensorResponse;
import com.arduino.simulator.model.Sensor;
import com.arduino.simulator.service.SensorService;
import com.arduino.simulator.service.LedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private LedService ledService;

    @GetMapping
    public ResponseEntity<Map<String, List<SensorResponse>>> getSensors(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String ids) {
        
        // Vérifier le threshold à chaque lecture des capteurs
        ledService.checkThreshold();

        List<Sensor> sensors;

        if (id != null) {
            // Single sensor
            Sensor sensor = sensorService.getSensorById(id);
            if (sensor == null) {
                return ResponseEntity.notFound().build();
            }
            sensors = Collections.singletonList(sensor);
        } else if (ids != null && !ids.isEmpty()) {
            // Multiple sensors
            List<Integer> sensorIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            sensors = sensorService.getSensorsByIds(sensorIds);
        } else {
            // All sensors
            sensors = sensorService.getAllSensors();
        }

        List<SensorResponse> sensorResponses = sensors.stream()
                .map(sensor -> new SensorResponse(
                        sensor.getId(),
                        sensor.getName(),
                        sensor.readValue(),
                        sensor.getUnit(),
                        sensor.getPos()
                ))
                .collect(Collectors.toList());

        Map<String, List<SensorResponse>> response = new HashMap<>();
        response.put("sensors", sensorResponses);

        return ResponseEntity.ok(response);
    }

    /**
     * Met à jour la valeur d'un capteur
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateSensorValue(
            @PathVariable int id,
            @RequestBody Map<String, Double> request) {

        Double value = request.get("value");
        if (value == null) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "La valeur est requise")
            );
        }

        boolean updated = sensorService.updateSensorValue(id, value);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        Sensor sensor = sensorService.getSensorById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Valeur du capteur mise à jour");
        response.put("sensor", new SensorResponse(
            sensor.getId(),
            sensor.getName(),
            sensor.readValue(),
            sensor.getUnit(),
            sensor.getPos()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Réinitialise un capteur en mode aléatoire
     */
    @PostMapping("/{id}/reset")
    public ResponseEntity<String> resetSensor(@PathVariable int id) {
        boolean reset = sensorService.resetSensor(id);
        if (!reset) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Capteur réinitialisé en mode aléatoire");
    }
}
