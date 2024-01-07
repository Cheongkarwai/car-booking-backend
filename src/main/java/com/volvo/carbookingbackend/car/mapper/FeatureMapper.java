package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.FeatureDTO;
import com.volvo.carbookingbackend.car.entity.Feature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FeatureMapper {

    @Mapping(source = "title",target = "title")
    @Mapping(source = "description",target = "description")
    @Mapping(source = "image.path",target = "image")
    FeatureDTO toFeatureDTO(Feature feature);
}
