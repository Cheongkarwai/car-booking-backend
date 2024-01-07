package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tbl_engine")
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "engine_generator")
    @SequenceGenerator(name = "engine_generator",sequenceName = "tbl_engine_seq",allocationSize = 1)
    private Long id;

    @Column(name = "no_of_cylinders")
    private int noOfCylinder;

    @ColumnDefault("0")
    private int capacity;

    @Column(name = "maximum_engine_power (hp @ rpm)")
    private String maximumEnginePower;

    @Column(name = "maximum_electric_power (hp @ rpm)")
    private String maximumElectricPower;

    @Column(name = "maximum_combined_power")
    private int maximumCombinedPower;

    @Column(name = "maximum_engine_torque (Nm @ rpm)")
    private String maximumEngineTorque;

    @Column(name = "maximum_combined_electric_torque (Nm)")
    private String maximumCombinedElectricTorque;

    @Column(name = "maximum_torque")
    private String maximumTorque;

    @Column(name = "transmission, gears")
    private String transmissionGears;

    @Column(name = "acceleration, 0-100 km/h (s)")
    private String acceleration;

    @Column(name = "tyre_size")
    private String tyreSize;

    @Column(name = "wheel_size (inch)")
    private String wheelSizeInInch;

    @Column(name = "drive_train")
    private String driveTrain;

    @Column(name = "fuel_tank_capacity (litres)")
    @ColumnDefault("0")
    private double fuelTankCapacityInLitres;

    @Column(name = "pure_electric_driving_range (km)")
    private double pureElectricDrivingRangeInKm;
}
