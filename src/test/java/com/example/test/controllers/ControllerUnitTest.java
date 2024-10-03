package com.example.test.controllers;

import com.example.test.Response.BatteryResponse;
import com.example.test.Response.Statistics;
import com.example.test.dto.BatteryDto;
import com.example.test.models.Battery;
import com.example.test.services.BatteryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
public class ControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatteryService batteryService;

    @Autowired
    private ObjectMapper objectMapper;

    List<BatteryDto> batteryDtoList = new ArrayList<>();
    List<Battery> batteryList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        batteryDtoList.clear();

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

        batteryList.clear();

        Battery battery1 = new Battery();
        battery1.setName("Battery 1");
        battery1.setPostcode(10000);
        battery1.setWattCapacity(100);

        Battery battery2 = new Battery();
        battery2.setName("Battery 2");
        battery2.setPostcode(20000);
        battery2.setWattCapacity(150);

        batteryList.add(battery1);
        batteryList.add(battery2);
    }

    @Test
    public void saveBetteriesTest() throws Exception {
        when(batteryService.saveBatteries(batteryDtoList)).thenReturn(batteryList);

        mockMvc.perform(post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batteryDtoList)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("Batteries saved"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void getBatteriesByRangeTest() throws Exception {
        String postcodeRange = "10000-20000";

        Statistics statistics = new Statistics(250,125.0);
        List<String> batteryNames = Arrays.asList("Battery 1", "Battery 2");
        BatteryResponse batteryResponse = new BatteryResponse(batteryNames,statistics);

        when(batteryService.getBatteryResponse(postcodeRange)).thenReturn(batteryResponse);

        mockMvc.perform(get("/batteries")
                        .param("postcodeRange", postcodeRange)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value("200"))
                        .andExpect(jsonPath("$.message").value("Batteries found in the given range"))
                        .andExpect(jsonPath("$.timestamp").exists())
                        .andExpect(jsonPath("$.response").isNotEmpty())
                        .andExpect(jsonPath("$.response.batteryNames").isArray())
                        .andExpect(jsonPath("$.response.batteryNames[0]").value("Battery 1"))
                        .andExpect(jsonPath("$.response.batteryNames[1]").value("Battery 2"))
                        .andExpect(jsonPath("$.response.statistics.totalWattCapacity").value(250))
                        .andExpect(jsonPath("$.response.statistics.averageWattCapacity").value(125.0));
    }
}
