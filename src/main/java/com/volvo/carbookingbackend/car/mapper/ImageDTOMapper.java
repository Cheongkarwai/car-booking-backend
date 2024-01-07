package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.car.dto.ImageDTO;
import com.volvo.carbookingbackend.car.entity.Category;
import com.volvo.carbookingbackend.car.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageDTOMapper {

    default List<ImageDTO> toImagesDTO(Set<Image> images, Category category){
        return images.parallelStream().filter(image->category.equals(image.getCategory()))
                .map(image->new ImageDTO(image.getPath(),image.getCategory()))
                .collect(Collectors.toList());

    }
}
