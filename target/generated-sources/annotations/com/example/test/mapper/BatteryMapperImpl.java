package com.example.test.mapper;

import com.example.test.dto.BatteryDto;
import com.example.test.models.Battery;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-06T12:06:46+0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class BatteryMapperImpl implements BatteryMapper {

    @Override
    public Battery toBattery(BatteryDto batteryDto) {
        if ( batteryDto == null ) {
            return null;
        }

        Battery battery = new Battery();

        battery.setName( batteryDto.getName() );
        battery.setPostcode( batteryDto.getPostcode() );
        battery.setWattCapacity( batteryDto.getWattCapacity() );

        return battery;
    }

    @Override
    public BatteryDto toBatteryDto(Battery battery) {
        if ( battery == null ) {
            return null;
        }

        BatteryDto batteryDto = new BatteryDto();

        batteryDto.setName( battery.getName() );
        batteryDto.setPostcode( battery.getPostcode() );
        batteryDto.setWattCapacity( battery.getWattCapacity() );

        return batteryDto;
    }

    @Override
    public List<Battery> toBatteryList(List<BatteryDto> batteryDtoList) {
        if ( batteryDtoList == null ) {
            return null;
        }

        List<Battery> list = new ArrayList<Battery>( batteryDtoList.size() );
        for ( BatteryDto batteryDto : batteryDtoList ) {
            list.add( toBattery( batteryDto ) );
        }

        return list;
    }

    @Override
    public List<BatteryDto> toBatteryDtoList(List<Battery> batteryList) {
        if ( batteryList == null ) {
            return null;
        }

        List<BatteryDto> list = new ArrayList<BatteryDto>( batteryList.size() );
        for ( Battery battery : batteryList ) {
            list.add( toBatteryDto( battery ) );
        }

        return list;
    }
}
