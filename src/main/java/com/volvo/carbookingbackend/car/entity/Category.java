package com.volvo.carbookingbackend.car.entity;

public enum Category {

    CAR_MODEL("car-model"),FEATURE("feature"),SHOWCASE("showcase"),
    INTERIOR("interior"), BROCHURE("brochure"), EXTERIOR("exterior");

    private String name;
    Category(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
