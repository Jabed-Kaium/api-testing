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

    @NotBlank
    private String name;

    @Min(value = 10000) @Max(value = 99999)
    private int postcode;

    @Min(value = 0)
    private int wattCapacity;

}
