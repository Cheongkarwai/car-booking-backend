package com.volvo.carbookingbackend.appointment.repository;

import com.volvo.carbookingbackend.appointment.entity.Appointment;
import com.volvo.carbookingbackend.appointment.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> ,JpaSpecificationExecutor<Appointment> {

    Page<Appointment> findAllByStatusEquals(Pageable pageable, Status status);

    long countAppointmentsByStatus(Status status);
}
