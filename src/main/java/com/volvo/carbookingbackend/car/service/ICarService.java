package com.volvo.carbookingbackend.car.service;

import com.volvo.carbookingbackend.car.dto.*;
import com.volvo.carbookingbackend.car.entity.Category;
import com.volvo.carbookingbackend.car.entity.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICarService {

    Page<CarDTO> findAll(Pageable pageable,String keyword,Condition condition);

    Page<CarDTO> findAllCarFeatures(Pageable pageable);

    CarDTO findById(Long id);

    List<CarModelProjection> findAllCarsModel(Condition condition);

    CarDTO findByModelName(String modelName);

    List<ColorDTO> findAllColorsByModelName(String modelName, Condition condition);

    void save(CarInput carInput);

    void save(CarInput carInput, MultipartFile [] featureImages,MultipartFile [] exteriorImages,MultipartFile carModelImage,
              MultipartFile [] interiorImages,MultipartFile brochure) throws IOException;

    void saveUsedCar(UsedCarInput usedCarInput, MultipartFile carModelImage) throws IOException;
    List<ConditionProjection> findAllConditions();

    void deleteById(Long id);

    CarDetailsDTO findCarDetailsById(Long id);

    void update(Long id,CarInput carInput, MultipartFile [] featureImages,MultipartFile [] exteriorImages,MultipartFile carModelImage,
                MultipartFile [] interiorImages,MultipartFile brochure) throws IOException;
}
