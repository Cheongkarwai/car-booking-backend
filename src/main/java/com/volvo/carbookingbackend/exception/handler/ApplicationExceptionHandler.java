package com.volvo.carbookingbackend.exception.handler;

import com.volvo.carbookingbackend.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;


@RestControllerAdvice
public class ApplicationExceptionHandler{

    @ExceptionHandler
    public ProblemDetail handleJwtException(JwtException exception,HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),exception.getMessage());
        problemDetail.setType(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleUsernameNotFound(UsernameNotFoundException exception,HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),exception.getMessage());
        problemDetail.setType(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleBadCredential(BadCredentialsException exception, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),exception.getMessage());
        problemDetail.setType(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail handleInvalidToken(InvalidTokenException exception, HttpServletRequest request){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),exception.getMessage());
        problemDetail.setType(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
