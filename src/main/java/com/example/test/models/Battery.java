package com.example.test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "batteries")
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private int postcode;

    private int wattCapacity;

    public Battery(int id, String name, int postcode, int wattCapacity) {
        this.id = id;
        this.name = name;
        this.postcode = postcode;
        this.wattCapacity = wattCapacity;
    }

    public Battery() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 10000)
    @Max(value = 99999)
    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(@Min(value = 10000) @Max(value = 99999) int postcode) {
        this.postcode = postcode;
    }

    @Min(value = 0)
    public int getWattCapacity() {
        return wattCapacity;
    }

    public void setWattCapacity(@Min(value = 0) int wattCapacity) {
        this.wattCapacity = wattCapacity;
    }
}
