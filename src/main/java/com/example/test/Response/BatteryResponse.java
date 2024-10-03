package com.example.test.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class BatteryResponse {
    private List<String> batteryNames;
    private Statistics statistics;

    public BatteryResponse(List<String> batteryNames, Statistics statistics) {
        this.batteryNames = batteryNames;
        this.statistics = statistics;
    }

    public List<String> getBatteryNames() {
        return batteryNames;
    }

    public void setBatteryNames(List<String> batteryNames) {
        this.batteryNames = batteryNames;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

}
