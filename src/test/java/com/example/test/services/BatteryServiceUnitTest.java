package com.example.test.services;

import com.example.test.Response.BatteryResponse;
import com.example.test.TestApplication;
import com.example.test.dto.BatteryDto;
import com.example.test.exception.BatteryNotFoundException;
import com.example.test.exception.InvalidBatteryListException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BatteryServiceUnitTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private BatteryMapper batteryMapper;

//    @InjectMocks
    private BatteryService batteryService;

    List<BatteryDto> batteryDtoList = new ArrayList<>();
    List<Battery> batteryList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        batteryService = new BatteryService(batteryRepository, batteryMapper);

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

        assertThat(savedBatteries.size()).isEqualTo(batteryList.size());
        verify(batteryRepository, Mockito.times(1)).saveAll(batteryList);
    }

    @Test
    public void emptyBatteryListThrowsException() {
        List<BatteryDto> emptyBatteryDtoList = new ArrayList<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> batteryService.saveBatteries(emptyBatteryDtoList));

        assertThat(exception.getMessage()).isEqualTo("Battery list cannot be empty");
        verify(batteryRepository, never()).saveAll(anyList());
    }

    @Test
    public void invalidBatteryListThrowsException() {
        BatteryDto batteryDto1 = new BatteryDto("", 10000, 100);
        BatteryDto batteryDto2 = new BatteryDto("Battery 2", 100, 150);
        BatteryDto batteryDto3 = new BatteryDto("Battery 3", 20000, -1);
        List<BatteryDto> invalidBatteryDtoList = Arrays.asList(batteryDto1, batteryDto2, batteryDto3);

        InvalidBatteryListException exception = assertThrows(InvalidBatteryListException.class, () -> batteryService.saveBatteries(invalidBatteryDtoList));

        assertThat(exception.getInvalidBatteryDtoList()).isEqualTo(invalidBatteryDtoList);
        verify(batteryRepository, never()).saveAll(anyList());
    }

    @Test
    public void getBatteriesTest() {

        when(batteryRepository.findByPostcodeBetween(10000, 20000)).thenReturn(batteryList);

        BatteryResponse batteryResponse = batteryService.getBatteryResponse("10000-20000");

        assertThat(batteryResponse).isNotNull();
        assertThat(batteryResponse.getBatteryNames().size()).isEqualTo(2);
        assertThat(batteryResponse.getBatteryNames().get(0)).isEqualTo("Battery 1");
        assertThat(batteryResponse.getStatistics().getTotalWattCapacity()).isEqualTo(250);
        assertThat(batteryResponse.getStatistics().getAverageWattCapacity()).isEqualTo(125.0);

    }

    @Test
    public void getBatteriesInvalidPostcodeRangeTestThrowsException() {
        String invalidPostcodeRange = "40000-30000";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> batteryService.getBatteryResponse(invalidPostcodeRange));

        assertThat(exception.getMessage()).isEqualTo("Invalid postcode range");

        verify(batteryRepository, never()).findByPostcodeBetween(anyInt(), anyInt());
    }

    @Test
    public void getBatteryResponseBatteryNotFoundTestThrowsException() {
        String postcodeRange = "40000-50000";

        BatteryNotFoundException exception = assertThrows(BatteryNotFoundException.class, () -> batteryService.getBatteryResponse(postcodeRange));

        assertThat(exception.getMessage()).isEqualTo("Batteries not found in given postcode range");
        verify(batteryRepository, times(1)).findByPostcodeBetween(anyInt(), anyInt());
    }
}
