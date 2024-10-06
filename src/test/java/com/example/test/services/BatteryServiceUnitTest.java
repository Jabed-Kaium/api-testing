package com.example.test.services;

import com.example.test.Response.BatteryResponse;
import com.example.test.TestApplication;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatteryServiceUnitTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private BatteryMapper batteryMapper;

    @InjectMocks
    private BatteryService batteryService;

    List<BatteryDto> batteryDtoList = new ArrayList<>();
    List<Battery> batteryList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        batteryDtoList.clear();
        batteryList.clear();

        BatteryDto batteryDto1 = new BatteryDto("Battery 1", 10000, 100);
        BatteryDto batteryDto2 = new BatteryDto("Battery 2", 20000, 150);

        batteryDtoList = Arrays.asList(batteryDto1, batteryDto2);

        System.out.println("Size of dto list: " + batteryDtoList.size());
        System.out.println(batteryDtoList);

        Battery battery1 = new Battery(1, "Battery 1", 10000, 100);
        Battery battery2 = new Battery(2, "Battery 2", 20000, 150);

        batteryList = Arrays.asList(battery1, battery2);

        System.out.println("Size of entity list: " + batteryList.size());
        System.out.println(batteryList);
    }

    @Test
    public void saveBatteriesTest() {
        when(batteryMapper.toBatteryList(batteryDtoList)).thenReturn(batteryList);
        when(batteryRepository.saveAll(batteryList)).thenReturn(batteryList);

        List<Battery> savedBatteries = batteryService.saveBatteries(batteryDtoList);

        Assertions.assertThat(savedBatteries.size()).isEqualTo(batteryList.size());
    }

    @Test
    public void getBatteriesTest() {

        when(batteryRepository.findByPostcodeBetween(10000, 20000)).thenReturn(batteryList);

        BatteryResponse batteryResponse = batteryService.getBatteryResponse("10000-20000");

        Assertions.assertThat(batteryResponse).isNotNull();
        Assertions.assertThat(batteryResponse.getBatteryNames().size()).isEqualTo(2);
        Assertions.assertThat(batteryResponse.getBatteryNames().get(0)).isEqualTo("Battery 1");
        Assertions.assertThat(batteryResponse.getStatistics().getTotalWattCapacity()).isEqualTo(250);
        Assertions.assertThat(batteryResponse.getStatistics().getAverageWattCapacity()).isEqualTo(125.0);

    }

}
