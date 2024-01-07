package com.volvo.carbookingbackend.car.dto;

import com.volvo.carbookingbackend.car.entity.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDTO {

    private String path;

    private Category category;

    public ImageDTO(String path, Category category){
        this.path = path;
        this.category = category;
    }
}
