package com.volvo.carbookingbackend.car.entity;

import com.volvo.carbookingbackend.appointment.entity.Appointment;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_generator")
    @SequenceGenerator(name = "car_generator", sequenceName = "tbl_car_seq", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    private String brand;

    @Column(unique = true)
    private String model;

//    @Enumerated(value = EnumType.STRING)
//    private Color color;

    @OneToMany(mappedBy = "car", cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private Set<CarDetails> carDetails = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<Feature> features = new HashSet<>();

    @Column(name = "starting_price")
    @ColumnDefault("0")
    private double startingPrice;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private String image;

    @ColumnDefault("0")
    private double milleage;

    private String pdfUrl;

    @OneToMany(mappedBy = "car", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Appointment> appointment = new HashSet<>();

    @OneToMany(mappedBy = "car", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Image> images = new HashSet<>();

    @Column(name = "brochure_pdf")
    private String brochurePdf;

    @Column(name = "model_image")
    private String modelImage;



//    public void addFeatures(Set<Feature> features){
//        System.out.println("Hi");
//        for(Feature feature : features){
//            feature.setCar(this);
//            this.features.add(feature);
//        }
//    }
//    public void addFeature(Feature feature){
//        System.out.println("Hi");
//        feature.setCar(this);
//        this.features.add(feature);
//    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setCar(this);
    }


    public void addImages(List<Image> images) {
        images.forEach(image -> {
            this.images.add(image);
            image.setCar(this);
        });
    }
}