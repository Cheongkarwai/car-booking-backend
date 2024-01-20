package com.volvo.carbookingbackend.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvo.carbookingbackend.car.entity.Condition;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarDetailsDTO{

    private Long id;

    private String brand;

    private String model;

    @JsonProperty("model_image")
    private String modelImage;

    @JsonProperty("brochure_pdf")
    private String brochurePdf;

    @JsonProperty("starting_price")
    private double startingPrice;

    private Condition condition;

    private double milleage;

    private List<FeatureDTO> features;

    @JsonProperty("exterior_images")
    private List<ImageDTO> exteriorImages;

    @JsonProperty("interior_images")
    private List<ImageDTO> interiorImages;


}
