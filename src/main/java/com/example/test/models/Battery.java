package com.example.test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Table(name = "batteries")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private int postcode;

    private int wattCapacity;

}
