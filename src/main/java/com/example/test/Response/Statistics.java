package com.example.test.Response;

public class Statistics {
    private int totalWattCapacity;
    private double averageWattCapacity;

    public Statistics(int totalWattCapacity, double averageWattCapacity) {
        this.totalWattCapacity = totalWattCapacity;
        this.averageWattCapacity = averageWattCapacity;
    }

    public int getTotalWattCapacity() {
        return totalWattCapacity;
    }

    public void setTotalWattCapacity(int totalWattCapacity) {
        this.totalWattCapacity = totalWattCapacity;
    }

    public double getAverageWattCapacity() {
        return averageWattCapacity;
    }

    public void setAverageWattCapacity(double averageWattCapacity) {
        this.averageWattCapacity = averageWattCapacity;
    }

}
