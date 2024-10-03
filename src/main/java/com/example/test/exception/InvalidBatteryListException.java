package com.example.test.exception;

import com.example.test.dto.BatteryDto;

import java.util.List;

public class InvalidBatteryListException  extends RuntimeException{
    private final List<BatteryDto> invalidBatteryDtoList;

    public InvalidBatteryListException(List<BatteryDto> invalidBatteryDtoList) {
        this.invalidBatteryDtoList = invalidBatteryDtoList;
    }

    public List<BatteryDto> getInvalidBatteryDtoList() {
        return invalidBatteryDtoList;
    }
}
