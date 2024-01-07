package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_battery")
public class Battery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
}
