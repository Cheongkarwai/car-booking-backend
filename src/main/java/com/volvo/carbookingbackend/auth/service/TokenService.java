package com.volvo.carbookingbackend.auth.service;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.TokenDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

    TokenDTO generateToken(UserDetails userDetails);

    TokenDTO validateRefreshToken(String refreshToken);

    boolean validateTokenValidity(String token);

    AccountDTO decodeJwt(String accessToken);
}

