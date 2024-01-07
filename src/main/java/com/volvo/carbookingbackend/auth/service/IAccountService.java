package com.volvo.carbookingbackend.auth.service;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.AuthorityDTO;
import com.volvo.carbookingbackend.auth.entity.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAccountService {

    Page<AccountDTO> findAll(Pageable pageable, Title title, String keyword);


    List<AuthorityDTO> findAllRoles();

    AccountDTO findById(String username);
}
