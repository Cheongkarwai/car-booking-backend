package com.volvo.carbookingbackend.appointment.specification;

import com.volvo.carbookingbackend.appointment.entity.Appointment;
import com.volvo.carbookingbackend.appointment.entity.Appointment_;
import com.volvo.carbookingbackend.appointment.entity.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class AppointmentSpecification  {

    public static Specification<Appointment> hasKeyword(String keyword){
        return (root, query, cb) ->
                cb.or(cb.like(root.get(Appointment_.emailAddress),"%"+keyword+"%"),cb.like(root.get(Appointment_.contactNumber),"%"+keyword+"%")
                ,cb.like(root.get(Appointment_.emailAddress),"%"+keyword+"%"));
    }

    public static Specification<Appointment> hasStatus(Status status){
        return (root, query, cb) ->{
            if(Objects.isNull(status)){
                return cb.isNull(root.get(Appointment_.status));
            }
            return cb.equal(root.get(Appointment_.status),status);
        };
    }
}
