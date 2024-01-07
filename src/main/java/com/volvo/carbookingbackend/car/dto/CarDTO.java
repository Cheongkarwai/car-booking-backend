package com.volvo.carbookingbackend.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvo.carbookingbackend.car.entity.Condition;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDTO{

    private Long id;

    private String brand;

    private String model;

    private double milleage;

    @JsonProperty("starting_price")
    private double startingPrice;

    private Condition condition;

    @JsonProperty("model_image")
    private String modelImage;

    private List<FeatureDTO> features;

    private List<ImageDTO> images;
}
