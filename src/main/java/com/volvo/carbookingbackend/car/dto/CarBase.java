package com.volvo.carbookingbackend.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvo.carbookingbackend.car.entity.Condition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarBase {

    private Long id;

    private String brand;

    private String model;

    @JsonProperty("starting_price")
    private double startingPrice;

    private Condition condition;
}
