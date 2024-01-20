package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.CarDetailsDTO;
import com.volvo.carbookingbackend.car.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {FeatureMapper.class})
public interface CarDetailsMapper{
    @ToCarBase
    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "modelImage",source = "modelImage"),
            @Mapping(target = "brochurePdf",source = "brochurePdf"),
    })
    CarDetailsDTO toCarDetailsDTO(Car car);

}