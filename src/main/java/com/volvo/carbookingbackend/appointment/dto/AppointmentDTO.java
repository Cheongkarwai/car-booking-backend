package com.volvo.carbookingbackend.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {

    private Long id;

    private String fullName;

    private String emailAddress;

    private String contactNumber;

    private LocalDateTime timestamp;
}
