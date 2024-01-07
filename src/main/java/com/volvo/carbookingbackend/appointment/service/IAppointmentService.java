package com.volvo.carbookingbackend.appointment.service;

import com.volvo.carbookingbackend.appointment.dto.AppointmentFormDTO;
import com.volvo.carbookingbackend.appointment.dto.AppointmentDTO;
import com.volvo.carbookingbackend.appointment.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface IAppointmentService {

    Page<AppointmentDTO> findAll(Pageable pageable, Status status,String keyword);

    void save(AppointmentFormDTO appointmentFormDTO);

    AppointmentDTO update(Long id, AppointmentDTO appointmentDTO);

    void partialUpdate(Long id, Map<String,Object> body) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException;

    Map<String,Object> count(Status[] status);
}
