package com.example.test.mapper;

import com.example.test.dto.BatteryDto;
import com.example.test.models.Battery;
import org.mapstruct.Mapper;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BatteryMapper {
    Battery toBattery(BatteryDto batteryDto);
    BatteryDto toBatteryDto(Battery battery);

    List<Battery> toBatteryList(List<BatteryDto> batteryDtoList);
    List<BatteryDto> toBatteryDtoList(List<Battery> batteryList);
}
