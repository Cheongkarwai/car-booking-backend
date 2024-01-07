package com.volvo.carbookingbackend.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentFormDTO {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("contact_number")
    private String contactNumber;

    @JsonProperty("model_name")
    private String modelName;

    private String remarks;
}
