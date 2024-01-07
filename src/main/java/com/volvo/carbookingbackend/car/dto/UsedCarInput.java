package com.volvo.carbookingbackend.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvo.carbookingbackend.car.entity.Condition;

public record UsedCarInput(String brand, String model, @JsonProperty("starting_price") int startingPrice
                        , Condition condition,
                           double milleage) {


}
