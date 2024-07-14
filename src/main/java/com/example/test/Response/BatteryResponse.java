package com.example.test.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteryResponse {
    private List<String> batteryNames;
    private Statistics statistics;
}
