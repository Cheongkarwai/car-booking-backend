package com.volvo.carbookingbackend.auth.repository;

import com.volvo.carbookingbackend.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByAccountUsername(String username);
}
