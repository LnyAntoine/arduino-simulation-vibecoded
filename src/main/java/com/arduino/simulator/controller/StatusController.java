package com.arduino.simulator.controller;

import com.arduino.simulator.dto.LedResponse;
import com.arduino.simulator.dto.SensorResponse;
import com.arduino.simulator.dto.ThresholdResponse;
import com.arduino.simulator.model.Sensor;
import com.arduino.simulator.model.Threshold;
import com.arduino.simulator.service.LedService;
import com.arduino.simulator.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private LedService ledService;

    @Autowired
    private SensorService sensorService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatus(
            @RequestParam(required = false) Integer led,
            @RequestParam(name = "sensor_ids", required = false) String sensorIds,
            @RequestParam(required = false) Integer threshold) {
        
        Map<String, Object> response = new HashMap<>();

        // LED info
        if (led != null && led == 1) {
            List<LedResponse> leds = new ArrayList<>();
            leds.add(new LedResponse(ledService.getStatus(), ledService.getLedPos()));
            response.put("leds", leds);
        }

        // Sensors info
        if (sensorIds != null && !sensorIds.isEmpty()) {
            List<Integer> ids = Arrays.stream(sensorIds.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            
            List<Sensor> sensors = sensorService.getSensorsByIds(ids);
            List<SensorResponse> sensorResponses = sensors.stream()
                    .map(sensor -> new SensorResponse(
                            sensor.getId(),
                            sensor.getName(),
                            sensor.readValue(),
                            sensor.getUnit(),
                            sensor.getPos()
                    ))
                    .collect(Collectors.toList());
            
            response.put("sensors", sensorResponses);
        }

        // Threshold info
        if (threshold != null && threshold == 1) {
            Threshold th = ledService.getThreshold();
            if (th != null) {
                List<ThresholdResponse> thresholds = new ArrayList<>();
                thresholds.add(new ThresholdResponse(
                        th.getSensor(),
                        th.getVal(),
                        th.getMode()
                ));
                response.put("thresholds", thresholds);
            }
        }

        if (threshold == null && (led == null || led != 1) && (sensorIds == null || sensorIds.isEmpty())) {
            List<Sensor> sensors = sensorService.getAllSensors();
            List<SensorResponse> sensorResponses = sensors.stream()
                    .map(sensor -> new SensorResponse(
                            sensor.getId(),
                            sensor.getName(),
                            sensor.readValue(),
                            sensor.getUnit(),
                            sensor.getPos()
                    ))
                    .collect(Collectors.toList());

            response.put("sensors", sensorResponses);

            Threshold th = ledService.getThreshold();
            if (th != null) {
                List<ThresholdResponse> thresholds = new ArrayList<>();
                thresholds.add(new ThresholdResponse(
                        th.getSensor(),
                        th.getVal(),
                        th.getMode()
                ));
                response.put("thresholds", thresholds);
            }

            List<LedResponse> leds = new ArrayList<>();
            leds.add(new LedResponse(ledService.getStatus(), ledService.getLedPos()));
            response.put("leds", leds);

        }

        return ResponseEntity.ok(response);
    }
}
