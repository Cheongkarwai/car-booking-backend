package com.volvo.carbookingbackend.car.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public interface CarModelProjection {

    Long getId();

    String getModel();

    String getPrice();

    List<String> getFile();

    @JsonProperty("model_image")
    String getModelImage();

    @JsonProperty("brochure_pdf")
    String getBrochurePdf();
}
