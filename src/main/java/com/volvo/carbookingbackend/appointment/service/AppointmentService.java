package com.volvo.carbookingbackend.appointment.service;

import com.volvo.carbookingbackend.appointment.dto.AppointmentFormDTO;
import com.volvo.carbookingbackend.appointment.dto.AppointmentDTO;
import com.volvo.carbookingbackend.appointment.entity.Appointment;
import com.volvo.carbookingbackend.appointment.entity.Status;
import com.volvo.carbookingbackend.appointment.repository.AppointmentRepository;
import com.volvo.carbookingbackend.appointment.specification.AppointmentSpecification;
import com.volvo.carbookingbackend.exception.AppointmentNotFoundException;
import com.volvo.carbookingbackend.exception.CarNotFoundException;
import com.volvo.carbookingbackend.car.entity.Car;
import com.volvo.carbookingbackend.car.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
@Transactional
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CarRepository carRepository;

    private final BeanUtilsBean beanUtilsBean;

    private final Function<Appointment, AppointmentDTO> appointmentDTOConverter = (s) -> AppointmentDTO.builder()
            .id(s.getId())
            .fullName(s.getFullName())
            .contactNumber(s.getContactNumber())
            .emailAddress(s.getEmailAddress())
            .timestamp(s.getTimestamp())
            .build();

    private final Function<AppointmentFormDTO, Appointment> appointmentFormDTOConverter = (s) -> Appointment.builder()
            .fullName(s.getFullName())
            .contactNumber(s.getContactNumber())
            .emailAddress(s.getEmailAddress())
            .build();

    public AppointmentService(AppointmentRepository appointmentRepository, BeanUtilsBean beanUtilsBean,
                             CarRepository carRepository) {
        this.appointmentRepository = appointmentRepository;
        this.beanUtilsBean = beanUtilsBean;
        this.carRepository = carRepository;
    }

    public Page<AppointmentDTO> findAll(Pageable pageable, Status status, String keyword) {

        Specification<Appointment> specification = Specification.where(null);

        specification = specification.and(AppointmentSpecification.hasStatus(status));

        if (StringUtils.hasText(keyword)) {
            specification = specification.and(AppointmentSpecification.hasKeyword(keyword));
        }

        return appointmentRepository.findAll(specification, Objects.requireNonNullElse(pageable, Pageable.ofSize(Integer.MAX_VALUE))).map(appointmentDTOConverter);
    }

    public void save(AppointmentFormDTO appointmentFormDTO) {

        Car car = carRepository.findByModel(appointmentFormDTO.getModelName())
                .orElseThrow(()->new CarNotFoundException("Car not found"));

        Appointment appointment = appointmentFormDTOConverter.apply(appointmentFormDTO);

        appointment.setCar(car);

        appointmentRepository.save(appointment);
    }

    @Override
    public AppointmentDTO update(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Appointment %s not found", id)));
        return appointmentDTOConverter.apply(appointmentRepository.save(appointment));
    }

    @Override
    public void partialUpdate(Long id, Map<String, Object> body) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        log.info("Partial update appointment");

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment id %s not found", id)));

        log.info("Before copy properties : Appointment id {} , status {}, contact number {}, email address {}, full name {}, timestamp {}", appointment.getId(), appointment.getStatus(), appointment.getContactNumber(), appointment.getEmailAddress(),
                appointment.getFullName(), appointment.getTimestamp());

        beanUtilsBean.populate(appointment, body);

        log.info("After copy properties : Appointment id {} , status {}, contact number {}, email address {}, full name {}, timestamp {}", appointment.getId(), appointment.getStatus(), appointment.getContactNumber(), appointment.getEmailAddress(),
                appointment.getFullName(), appointment.getTimestamp());

        appointmentRepository.save(appointment);
    }

    public Map<String, Object> count(Status[] statusList) {


        Map<String, Object> countMap = new HashMap<>();

        for (Status status : statusList) {
            long count = this.appointmentRepository.countAppointmentsByStatus(status);
            if (status == null) {
                countMap.put("PENDING", count);
            } else {
                countMap.put(status.name(), count);
            }
        }

        return countMap;
    }
}
