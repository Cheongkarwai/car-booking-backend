package com.volvo.carbookingbackend.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvo.carbookingbackend.car.entity.Condition;

import java.util.List;

public record CarInput(String model,String brand,
                       @JsonProperty("starting_price") int startingPrice,
                       Condition condition,
                       List<FeatureInput> features,
                       double milleage) {
}
