package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.CarInput;
import com.volvo.carbookingbackend.car.dto.UsedCarInput;
import com.volvo.carbookingbackend.car.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsedCarMapper {

    @ToCarBase
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "milleage",target = "milleage")
    })
    Car toUsedCar(UsedCarInput carInput);
}
