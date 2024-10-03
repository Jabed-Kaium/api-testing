package com.example.test.dto;


public class BatteryDto {
    private String name;
    private int postcode;
    private int wattCapacity;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWattCapacity() {
        return wattCapacity;
    }

    public void setWattCapacity(int wattCapacity) {
        this.wattCapacity = wattCapacity;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }
}
