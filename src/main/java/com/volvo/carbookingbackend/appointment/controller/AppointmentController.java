package com.volvo.carbookingbackend.appointment.controller;

import com.volvo.carbookingbackend.appointment.dto.AppointmentFormDTO;
import com.volvo.carbookingbackend.appointment.dto.AppointmentDTO;
import com.volvo.carbookingbackend.appointment.entity.Status;
import com.volvo.carbookingbackend.appointment.service.IAppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    public AppointmentController(IAppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public HttpEntity<Page<AppointmentDTO>> getAppointments(Pageable pageable, @RequestParam(required = false) Status status,
                                                            @RequestParam(required = false) String keyword){
        return ResponseEntity.ok(appointmentService.findAll(pageable,status,keyword));
    }

    @GetMapping("/count")
    public HttpEntity<Map<String,Object>> countAppointments(@RequestParam(required = false,value = "status") Status[] statuses){

        return ResponseEntity.ok(appointmentService.count(statuses));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveAppointment(@RequestBody AppointmentFormDTO appointmentFormDTO){
        appointmentService.save(appointmentFormDTO);
    }

    @PutMapping("/{id}")
    public HttpEntity<AppointmentDTO> update(@PathVariable Long id, @RequestBody  AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.update(id,appointmentDTO));
    }

    @PatchMapping("/{id}")
    public void partialUpdate(@PathVariable Long id,@RequestBody Map<String,Object> body) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        appointmentService.partialUpdate(id,body);
    }
}
