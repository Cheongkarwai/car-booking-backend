package com.volvo.carbookingbackend.car.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "brand", source="brand")
@Mapping(target = "model", source = "model")
@Mapping(target = "startingPrice",source = "startingPrice")
@Mapping(target = "condition",source = "condition")
public @interface ToCarBase {
}
