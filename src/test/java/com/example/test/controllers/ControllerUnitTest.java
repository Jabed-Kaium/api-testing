package com.example.test.controllers;

import com.example.test.Response.BatteryResponse;
import com.example.test.Response.Statistics;
import com.example.test.dto.BatteryDto;
import com.example.test.exception.BatteryNotFoundException;
import com.example.test.exception.InvalidBatteryListException;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
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

        batteryDtoList.clear();

        BatteryDto batteryDto1 = new BatteryDto("Battery 1", 10000, 100);
        BatteryDto batteryDto2 = new BatteryDto("Battery 2", 20000, 150);

        batteryDtoList = Arrays.asList(batteryDto1, batteryDto2);

        batteryList.clear();

        Battery battery1 = new Battery(1, "Battery 1", 10000, 100);
        Battery battery2 = new Battery(2, "Battery 2", 20000, 150);

        batteryList = Arrays.asList(battery1, battery2);
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
    public void invalidBatteryListShouldReturnBadRequest() throws Exception {
        BatteryDto batteryDto = new BatteryDto();
        batteryDto.setName("");
        batteryDto.setPostcode(10000);
        batteryDto.setWattCapacity(100);

        List<BatteryDto> invalidBatteries = Arrays.asList(batteryDto);

        //mock the behavior of service to throw InvalidBatteryListException
        doThrow(new InvalidBatteryListException(invalidBatteries)).when(batteryService).saveBatteries(anyList());

        mockMvc.perform(post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBatteries)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid input detected."))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.invalidBatteries").isNotEmpty());
    }

    @Test
    public void emptyBatteryListShouldReturnBadRequest() throws Exception {
        List<Battery> emptyBatteryDtoList = new ArrayList<>();

        //mock the behavior of service to throw illegalArgumentException
        doThrow(new IllegalArgumentException("Battery list cannot be empty")).when(batteryService).saveBatteries(anyList());

        mockMvc.perform(post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyBatteryDtoList)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Battery list cannot be empty"))
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

    @Test
    public void emptyPostcodeRangeShouldReturnBadRequest() throws Exception {
        String postcodeRange = "";

        doThrow(new IllegalArgumentException("Postcode range cannot be empty")).when(batteryService).getBatteryResponse(postcodeRange);

        mockMvc.perform(get("/batteries")
                .param("postcodeRange", postcodeRange)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Postcode range cannot be empty"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void invalidPostcodeRangeShouldReturnBadRequest() throws Exception {
        String postcodeRange = "20000-10000";

        doThrow(new IllegalArgumentException("Invalid postcode range")).when(batteryService).getBatteryResponse(postcodeRange);

        mockMvc.perform(get("/batteries")
                .param("postcodeRange", postcodeRange)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid postcode range"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void shouldReturnBatteryNotFound() throws Exception {
        String postcodeRange = "30000-40000";

        doThrow(new BatteryNotFoundException("Batteries not found in given postcode range")).when(batteryService).getBatteryResponse(postcodeRange);

        mockMvc.perform(get("/batteries")
                .param("postcodeRange", postcodeRange)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Batteries not found in given postcode range"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
