package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_color")
public class Colors {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "color_generator")
    @SequenceGenerator(name="color_generator",sequenceName = "tbl_color_seq",allocationSize = 1)
    private Long id;

    private String name;

    private String code;

    @OneToMany(mappedBy = "colors")
    private Set<CarDetails> carStocks = new HashSet<>();
}
