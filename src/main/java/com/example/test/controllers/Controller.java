package com.example.test.controllers;

import com.example.test.Response.BatteryResponse;
import com.example.test.Response.Statistics;
import com.example.test.dto.BatteryDto;
import com.example.test.models.Battery;
import com.example.test.services.BatteryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
public class Controller {

    @Autowired
    private BatteryService batteryService;

    //endpoint for saving batteries
    @PostMapping("/batteries")
    public ResponseEntity<Map<String, Object>> addBatteries(@RequestBody @Valid List<BatteryDto> batteries) {
        List<Battery> savedBatteries = batteryService.saveBatteries(batteries);
        Map<String, Object> response = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        response.put("timestamp", now.toString());
        response.put("status", "201");
        response.put("message","Batteries saved");
        response.put("batteries", savedBatteries);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //endpoint for retrieving batteries with given range
    @GetMapping("/batteries")
    public ResponseEntity<Map<String, Object>> getBatteriesByRange(@RequestParam("postcodeRange") String postcodeRange) {
        validatePostcodeRange(postcodeRange);
        BatteryResponse batteryResponse = batteryService.getBatteryResponse(postcodeRange);

        Map<String, Object> response = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        response.put("timestamp", now.toString());
        response.put("status", "200");
        response.put("message","Batteries found in the given range");
        response.put("response", batteryResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private void validatePostcodeRange(String postcodeRange) {
        if (postcodeRange == null || postcodeRange.isEmpty()) {
            throw new IllegalArgumentException("Postcode range cannot be empty");
        }
    }
}
