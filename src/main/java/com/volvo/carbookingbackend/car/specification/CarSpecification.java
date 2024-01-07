package com.volvo.carbookingbackend.car.specification;

import com.volvo.carbookingbackend.car.entity.Car;
import com.volvo.carbookingbackend.car.entity.Car_;
import com.volvo.carbookingbackend.car.entity.Condition;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {

    public static Specification<Car> hasKeyword(String keyword) {
        return (root, query, cb) -> cb.or(cb.like(root.get(Car_.brand), "%" + keyword + "%")
                , cb.like(root.get(Car_.model), "%" + keyword + "%"),
                cb.like(root.get(Car_.startingPrice).as(String.class), "%" + keyword + "%"));
    }

    public static Specification<Car> hasCondition(Condition condition) {
        return (root,query,cb) -> cb.equal(root.get(Car_.condition),condition);
    }
}
