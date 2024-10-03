package com.example.test.repositories;

import com.example.test.models.Battery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
public class BatteryRepositoryUnitTest {

    @Autowired
    private BatteryRepository batteryRepository;

    List<Battery> batteryList = new ArrayList<>();

    @BeforeEach
    public void setUp() {

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
        //action
        batteryRepository.saveAll(batteryList);

        //verify
        List<Battery> batteryList2 = batteryRepository.findAll();
        Assertions.assertThat(batteryList2.size()).isEqualTo(3);
    }


    @Test
    public void findPostCodeBetweenTest() {
        //action
        batteryRepository.saveAll(batteryList);
        List<Battery> batteryList3 = batteryRepository.findByPostcodeBetween(15000,25000);
        List<Battery> batteryList4 = batteryRepository.findByPostcodeBetween(40000,50000);

        //verify
        Assertions.assertThat(batteryList3.size()).isEqualTo(1);
        Assertions.assertThat(batteryList4.size()).isEqualTo(0);
    }
}
