package com.example.test.controllers;

import com.example.test.Response.BatteryResponse;
import com.example.test.Response.Statistics;
import com.example.test.models.Battery;
import com.example.test.services.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Autowired
    private BatteryService batteryService;

    //endpoint for saving batteries
    @PostMapping("/batteries")
    public List<Battery> addBatteries(@RequestBody List<Battery> batteries) {
        return batteryService.saveBatteries(batteries);
    }


    //endpoint for retrieving batteries with given range
    @GetMapping("/batteries")
    public ResponseEntity<BatteryResponse> getBatteriesByRange(@RequestParam("postcodeRange") String postcodeRange) {

        String[] postcodes = postcodeRange.split("-");
        String mincode = postcodes[0];
        String maxcode = postcodes[1];

        //list of batteries in given range
        List<Battery> batteries = batteryService.getBatteriesByRange(mincode, maxcode);

        //list of battery names
        List<String> batteryNames = batteries.stream()
                .map(Battery::getName)
                .collect(Collectors.toList());

        Collections.sort(batteryNames);

        int totalWattCapacity = batteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .sum();

        double averageWattCapacity = batteries.isEmpty() ? 0 : totalWattCapacity / batteries.size();

        Statistics statistics = new Statistics(totalWattCapacity, averageWattCapacity);
        BatteryResponse batteryResponse = new BatteryResponse(batteryNames, statistics);

        return ResponseEntity.ok(batteryResponse);
    }
}
