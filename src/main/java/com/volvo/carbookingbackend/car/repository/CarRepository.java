package com.volvo.carbookingbackend.car.repository;

import com.volvo.carbookingbackend.car.dto.CarModelProjection;
import com.volvo.carbookingbackend.car.entity.Car;
import com.volvo.carbookingbackend.car.entity.Category;
import com.volvo.carbookingbackend.car.entity.Condition;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Long> , JpaSpecificationExecutor<Car> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "features"
            }
    )
    Page<Car> findAll(@Nullable Pageable pageable);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "features",
                    "images"
            }
    )
    Optional<Car> findById(Long id);

    @Query("FROM Car u JOIN FETCH u.features")
    Page<Car> findAllCarFeatures(Pageable pageable);
    Optional<Car> findByModel(String model);

    @Query("SELECT c.id as id, c.model as model,c.startingPrice as price,c.brochurePdf as brochurePdf,c.modelImage as modelImage FROM Car c WHERE c.condition = :condition")
    List<CarModelProjection> findAllCarsModel(@Param("condition") Condition condition);
}
