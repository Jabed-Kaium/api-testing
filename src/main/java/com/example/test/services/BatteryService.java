package com.example.test.services;

import com.example.test.Response.BatteryResponse;
import com.example.test.Response.Statistics;
import com.example.test.dto.BatteryDto;
import com.example.test.exception.BatteryNotFoundException;
import com.example.test.exception.InvalidBatteryListException;
import com.example.test.mapper.BatteryMapper;
import com.example.test.models.Battery;
import com.example.test.repositories.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    private final BatteryMapper batteryMapper;

    @Autowired
    public BatteryService(BatteryRepository batteryRepository, BatteryMapper batteryMapper) {
        this.batteryRepository = batteryRepository;
        this.batteryMapper = batteryMapper;
    }

    /*
    * receive list of BatteryDto and convert it to list of Battery Entity then save
    * */
    public List<Battery> saveBatteries(List<BatteryDto> batteries) {

        List<BatteryDto> invalidList = validateBatteryList(batteries);

        boolean isBatteryListValid = invalidList.isEmpty();

        if(!isBatteryListValid) {
            throw new InvalidBatteryListException(invalidList);
        }

        if(batteries.isEmpty()) {
            throw new IllegalArgumentException("Battery list cannot be empty");
        }

        List<Battery> batteryList = batteryMapper.toBatteryList(batteries);
        return batteryRepository.saveAll(batteryList);
    }

    public BatteryResponse getBatteryResponse(String postcodeRange) {

        String[] postcodes = postcodeRange.split("-");
        int mincode = Integer.parseInt(postcodes[0]);
        int maxcode = Integer.parseInt(postcodes[1]);

        if(mincode > maxcode) {
            throw new IllegalArgumentException("Invalid postcode range");
        }

        //list of batteries in given range
        List<Battery> batteries = batteryRepository.findByPostcodeBetween(mincode,maxcode);

        if(batteries.isEmpty()) {
            throw new BatteryNotFoundException("Batteries not found in given postcode range");
        }


        //list of battery names
        List<String> batteryNames = batteries.stream()
                .map(Battery::getName)
                .collect(Collectors.toList());

        Collections.sort(batteryNames);

        int totalWattCapacity = batteries.stream()
                .mapToInt(Battery::getWattCapacity)
                .sum();

        double averageWattCapacity = batteries.isEmpty() ? 0 : (double) totalWattCapacity / batteries.size();

        Statistics statistics = new Statistics(totalWattCapacity, averageWattCapacity);
        return new BatteryResponse(batteryNames, statistics);
    }

    /*
    * validation for battery list
    * */
    private List<BatteryDto> validateBatteryList(List<BatteryDto> batteries) {

        List<BatteryDto> invalidDtoList = new ArrayList<>();

        for(int i=0; i<batteries.size(); i++) {
            if(!isBatteryDtoValid(batteries.get(i))) {
                invalidDtoList.add(batteries.get(i));
            }
        }

        return invalidDtoList;
    }

    private boolean isBatteryDtoValid(BatteryDto batteryDto) {
        if(batteryDto == null || batteryDto.getName().length() == 0 || batteryDto.getPostcode() < 10000 || batteryDto.getPostcode() > 99999 || batteryDto.getWattCapacity() < 0) {
            return false;
        }

        return true;
    }

}
