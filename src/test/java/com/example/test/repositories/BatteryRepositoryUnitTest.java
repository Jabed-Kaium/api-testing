package com.example.test.repositories;

import com.example.test.models.Battery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@DataJpaTest
public class BatteryRepositoryUnitTest {

    @Mock
    private BatteryRepository batteryRepository;

    List<Battery> batteryList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        batteryList.clear();

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

        Battery battery3 = new Battery();
        battery3.setId(3);
        battery3.setName("Battery 3");
        battery3.setPostcode(30000);
        battery3.setWattCapacity(200);

        batteryList.add(battery1);
        batteryList.add(battery2);
        batteryList.add(battery3);
    }

    @Test
    public void saveAllBatteryTest() {
        when(batteryRepository.saveAll(batteryList)).thenReturn(batteryList);

        List<Battery> batteries = batteryRepository.saveAll(batteryList);

        Assertions.assertThat(batteries).isEqualTo(batteryList);
    }


    @Test
    public void findPostCodeBetweenTest() {
        List<Battery> batteries = Arrays.asList(new Battery(2, "Battery 2", 20000, 150));

        when(batteryRepository.findByPostcodeBetween(15000,25000)).thenReturn(batteries);

        List<Battery> batteries1 = batteryRepository.findByPostcodeBetween(15000,25000);

        //verify
        Assertions.assertThat(batteries1).isEqualTo(batteries);
    }
}
