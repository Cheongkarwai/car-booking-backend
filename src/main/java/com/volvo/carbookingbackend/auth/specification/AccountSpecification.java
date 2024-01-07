package com.volvo.carbookingbackend.auth.specification;

import com.volvo.carbookingbackend.appointment.entity.Appointment;
import com.volvo.carbookingbackend.appointment.entity.Appointment_;
import com.volvo.carbookingbackend.appointment.entity.Status;
import com.volvo.carbookingbackend.auth.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Set;

public class AccountSpecification {

        public static Specification<Account> hasKeyword(String keyword){
            return (root, query, cb) ->
                    cb.or(cb.like(root.get(Account_.username),"%"+keyword+"%"),cb.like(root.get(Account_.fullName),"%"+keyword+"%")
                            ,cb.like(root.get(Account_.contactNumber),"%"+keyword+"%"));
        }

        public static Specification<Account> hasTitle(Title title){
            return (root, query, cb) -> {
                System.out.println(title);
                return cb.equal(root.join("authorities").get("title"), title);
               // cb.like(root.join(Account_.authorities).join(Authority_.accounts).get("title").as(String.class),"%"+title.name()+"%");
            };

        }

}
