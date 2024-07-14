package com.example.test.services;

import com.example.test.models.Battery;
import com.example.test.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryService {

    @Autowired
    private BatteryRepository batteryRepository;

    //save batteries to database
    public List<Battery> saveBatteries(List<Battery> batteries) {
        return batteryRepository.saveAll(batteries);
    }

    //get batteries with given range from database
    public List<Battery> getBatteriesByRange(String mincode, String maxcode) {
        return batteryRepository.findByPostcodeRange(mincode,maxcode);
    }
}
