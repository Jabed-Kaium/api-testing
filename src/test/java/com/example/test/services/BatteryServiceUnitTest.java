package com.example.test.services;

import com.example.test.Response.BatteryResponse;
import com.example.test.dto.BatteryDto;
import com.example.test.exception.BatteryNotFoundException;
import com.example.test.mapper.BatteryMapper;
import com.example.test.models.Battery;
import com.example.test.repositories.BatteryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BatteryServiceUnitTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private BatteryMapper batteryMapper;


    @InjectMocks @Autowired
    private BatteryService batteryService;

    List<BatteryDto> batteryDtoList = new ArrayList<>();
    List<Battery> batteryList = new ArrayList<>();

    List<Battery> batteries = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        batteryDtoList.clear();
        batteryList.clear();

        BatteryDto batteryDto1 = new BatteryDto();
        batteryDto1.setName("Battery 1");
        batteryDto1.setPostcode(10000);
        batteryDto1.setWattCapacity(100);

        BatteryDto batteryDto2 = new BatteryDto();
        batteryDto2.setName("Battery 2");
        batteryDto2.setPostcode(20000);
        batteryDto2.setWattCapacity(150);

        batteryDtoList.add(batteryDto1);
        batteryDtoList.add(batteryDto2);

        System.out.println("Size of dto list: " + batteryDtoList.size());
        System.out.println(batteryDtoList);

        Battery battery1 = new Battery();
        battery1.setId(1);
        battery1.setName("Battery 1");
        battery1.setPostcode(10000);
        battery1.setWattCapacity(100);

        Battery battery2 = new Battery();
        battery2.setId(2);
        battery2.setName("Battery 2");
        battery2.setPostcode(20000);
        battery2.setWattCapacity(150);

        batteryList.add(battery1);
        batteryList.add(battery2);

        System.out.println("Size of entity list: " + batteryList.size());
        System.out.println(batteryList);
    }

    @Test
    public void saveBatteriesTest() {

        batteries = batteryService.saveBatteries(batteryDtoList);

        Assertions.assertThat(batteries.size()).isEqualTo(batteryList.size());
    }

    @Test
    public void getBatteriesTest() {

        BatteryResponse batteryResponse = batteryService.getBatteryResponse("10000-20000");
        Assertions.assertThat(batteryResponse).isNotNull();
        Assertions.assertThat(batteryResponse.getBatteryNames().size()).isEqualTo(2);
        Assertions.assertThat(batteryResponse.getBatteryNames().get(0)).isEqualTo("Battery 1");
        Assertions.assertThat(batteryResponse.getStatistics().getTotalWattCapacity()).isEqualTo(250);
        Assertions.assertThat(batteryResponse.getStatistics().getAverageWattCapacity()).isEqualTo(125.0);

    }

}
