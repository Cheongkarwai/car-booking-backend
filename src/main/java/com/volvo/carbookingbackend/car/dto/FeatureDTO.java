package com.volvo.carbookingbackend.car.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureDTO {

    private String title;

    private String description;

    private String image;
}
