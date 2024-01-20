package com.volvo.carbookingbackend.car.repository;

import com.volvo.carbookingbackend.car.dto.ConditionProjection;
import com.volvo.carbookingbackend.car.entity.CarDetails;
import com.volvo.carbookingbackend.car.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarDetailsRepository extends JpaRepository<CarDetails,Long> {

    List<CarDetails> findAllByCarModelAndCondition(String modelName, Condition condition);

}
