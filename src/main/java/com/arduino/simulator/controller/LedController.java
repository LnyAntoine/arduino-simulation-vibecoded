package com.arduino.simulator.controller;

import com.arduino.simulator.dto.LedResponse;
import com.arduino.simulator.dto.ThresholdRequest;
import com.arduino.simulator.dto.ThresholdResponse;
import com.arduino.simulator.model.Threshold;
import com.arduino.simulator.service.LedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/led")
public class LedController {

    @Autowired
    private LedService ledService;

    @PostMapping("/on")
    public ResponseEntity<String> ledOn() {
        ledService.ledOn();
        return ResponseEntity.ok("Led allumée");
    }

    @PostMapping("/off")
    public ResponseEntity<String> ledOff() {
        ledService.ledOff();
        return ResponseEntity.ok("Led éteinte");
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Integer>> toggle() {
        ledService.toggle();
        Map<String, Integer> response = new HashMap<>();
        response.put("status", ledService.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<LedResponse> getLedInfo() {
        LedResponse response = new LedResponse(
            ledService.getStatus(),
            ledService.getLedPos()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/threshold")
    public ResponseEntity<String> setThreshold(@RequestBody ThresholdRequest request) {
        ledService.setSensorThreshold(request.getSensor(), request.getVal(), request.getMode());
        return ResponseEntity.ok("Threshold modifié");
    }

    @DeleteMapping("/threshold")
    public ResponseEntity<String> deleteThreshold() {
        ledService.deleteThreshold();
        return ResponseEntity.ok("Threshold supprimé");
    }

    @GetMapping("/threshold")
    public ResponseEntity<ThresholdResponse> getThreshold() {
        Threshold threshold = ledService.getThreshold();
        if (threshold == null) {
            return ResponseEntity.notFound().build();
        }
        
        ThresholdResponse response = new ThresholdResponse(
            threshold.getSensor(),
            threshold.getVal(),
            threshold.getMode()
        );
        return ResponseEntity.ok(response);
    }
}
