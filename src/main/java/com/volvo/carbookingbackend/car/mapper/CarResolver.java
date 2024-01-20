package com.volvo.carbookingbackend.car.mapper;

import com.volvo.carbookingbackend.auth.dto.UserInput;
import com.volvo.carbookingbackend.car.dto.CarInput;
import com.volvo.carbookingbackend.car.entity.Car;
import com.volvo.carbookingbackend.car.repository.CarRepository;
import com.volvo.carbookingbackend.exception.CarNotFoundException;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarResolver {

    private CarRepository carRepository;
    public CarResolver(CarRepository carRepository){
        this.carRepository = carRepository;
    }
//    @ObjectFactory
//    public Car resolve(CarInput dto, @TargetType Class<Car> type) {
//        return dto != null && dto.id() != null ? carRepository.findById(dto.id()).orElseThrow(()->new CarNotFoundException("Car not found")): new Car();
//    }
}
