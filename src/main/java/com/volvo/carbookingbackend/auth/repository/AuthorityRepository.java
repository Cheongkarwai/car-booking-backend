package com.volvo.carbookingbackend.auth.repository;

import com.volvo.carbookingbackend.auth.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
