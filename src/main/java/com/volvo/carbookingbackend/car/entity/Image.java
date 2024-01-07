package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tbl_image_seq_generator")
    @SequenceGenerator(name = "tbl_image_seq_generator",sequenceName = "tbl_image_seq",allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "text")
    private String path;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id",referencedColumnName = "id")
    private Car car;

    @OneToOne(mappedBy = "image")
    private Feature feature;
}
