package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name="tbl_car_details")
public class CarDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "car_details_generator")
    @SequenceGenerator(name="car_details_generator",sequenceName = "tbl_car_details_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name="car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(unique = true,name = "color_id")
    private Colors colors;

    private int quantity;

    @Enumerated(value = EnumType.STRING)
    private Condition condition;

    @Column(name = "starting_price")
    @ColumnDefault("0")
    private double startingPrice;

    @OneToOne
    private Engine engine;

}
