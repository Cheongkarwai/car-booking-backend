package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.ImageDTO;
import com.volvo.carbookingbackend.car.entity.Category;
import com.volvo.carbookingbackend.car.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {

    Image toImage(String path);

    @Mappings({
            @Mapping(source = "path",target = "path"),
            @Mapping(source="category",target = "category")
    })
    ImageDTO toImageDTO(Image image);

    default List<Image> toImages(List<String> paths, Category category){

        return paths.parallelStream().map(path->{
            Image image = new Image();
            image.setPath(path);
            image.setCategory(category);
            return image;
        }).collect(Collectors.toList());
    }
}
