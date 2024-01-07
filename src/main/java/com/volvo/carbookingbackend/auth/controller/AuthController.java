package com.volvo.carbookingbackend.auth.controller;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.AccountInput;
import com.volvo.carbookingbackend.auth.dto.TokenDTO;
import com.volvo.carbookingbackend.auth.service.TokenService;
import com.volvo.carbookingbackend.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

//@Component
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;

    private final UserService userDetailsService;

    private final DaoAuthenticationProvider daoAuthenticationProvider;


    public AuthController(TokenService tokenService, UserService userDetailsService, DaoAuthenticationProvider daoAuthenticationProvider){
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @PostMapping ("/login")
    public HttpEntity<AccountDTO> login(@RequestParam Map<String, String> body) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Authentication authentication = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(body.get("username"),body.get("password")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        TokenDTO tokenDTO = tokenService.generateToken(userDetails);
        AccountDTO accountDTO = tokenService.decodeJwt(tokenDTO.getAccessToken());
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token",
                        tokenDTO.getAccessToken())
                .httpOnly(true).path("/").domain("localhost").maxAge(60*60*24)
                .secure(true).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token",
                tokenDTO.getRefreshToken())
                .httpOnly(true).path("/").domain("localhost").maxAge(60*60*24)
                .secure(true).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return new ResponseEntity<>(accountDTO,headers,HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public HttpEntity<TokenDTO> refreshToken(@CookieValue(name="refresh_token",required = false,defaultValue = "") String refreshToken){
        TokenDTO tokenDTO = tokenService.validateRefreshToken(refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token",
                        tokenDTO.getAccessToken())
                .httpOnly(true).path("/").domain("localhost").maxAge(60*60*24)
                .secure(false).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token",
                        tokenDTO.getRefreshToken())
                .httpOnly(true).path("/").domain("localhost").maxAge(60*60*24)
                .secure(false).build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return new ResponseEntity<>(tokenDTO,headers,HttpStatus.OK);

    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@RequestBody AccountInput accountInput){
        userDetailsService.createUser(accountInput);
    }

    @PostMapping("/secrets")
    public HttpEntity<TokenDTO> findUserCredentials(@CookieValue(name="access_token",required = false,defaultValue = "") String accessToken,
                                                    @CookieValue(name="refresh_token",required = false,defaultValue = "") String refreshToken) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        if(!(StringUtils.hasText(accessToken) || StringUtils.hasText(refreshToken))){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }

        if(!tokenService.validateTokenValidity(accessToken)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }

        return ResponseEntity.ok(TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build());
    }

    @PostMapping("/logout")
    public HttpEntity<Void> logout(HttpServletRequest request){
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token",
                        "")
                .httpOnly(true).path("/").domain("localhost").maxAge(0)
                .secure(false).build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token",
                        "")
                .httpOnly(true).path("/").domain("localhost").maxAge(0)
                .secure(false).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return new ResponseEntity<>(null,headers,HttpStatus.OK);
    }
}

