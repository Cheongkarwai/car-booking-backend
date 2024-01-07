package com.volvo.carbookingbackend.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("contact_number")
    private String contactNumber;

    private List<SimpleGrantedAuthority> authorities;

}
