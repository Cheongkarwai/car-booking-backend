package com.volvo.carbookingbackend.car.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_feature")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tbl_feature_seq_generator")
    @SequenceGenerator(name = "tbl_feature_seq_generator",allocationSize = 1)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    private Image image;

    public void setImage(Image image){
        image.setFeature(this);
        this.image = image;
    }
}
