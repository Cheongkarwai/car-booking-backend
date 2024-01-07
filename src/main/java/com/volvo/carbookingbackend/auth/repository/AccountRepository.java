package com.volvo.carbookingbackend.auth.repository;

import com.volvo.carbookingbackend.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account,String>, JpaSpecificationExecutor<Account> {
}
