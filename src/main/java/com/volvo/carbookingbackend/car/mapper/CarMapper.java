package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.CarDTO;
import com.volvo.carbookingbackend.car.dto.CarDetailsDTO;
import com.volvo.carbookingbackend.car.dto.CarInput;
import com.volvo.carbookingbackend.car.dto.FeatureInput;
import com.volvo.carbookingbackend.car.entity.Car;
import com.volvo.carbookingbackend.car.entity.Feature;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = {FeatureMapper.class, ImageMapper.class})
public interface CarMapper {
    @ToCarBase
    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    Car toCar(CarInput carInput);

    @ToCarBase
    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "milleage",source = "milleage"),
            @Mapping(target = "modelImage",source = "modelImage")
    })
    CarDTO toCarDTO(Car car);

    @AfterMapping
    default void setCar(@MappingTarget Car car){
        car.getFeatures().forEach(feature -> {
            feature.setCar(car);
        });
    }



}
