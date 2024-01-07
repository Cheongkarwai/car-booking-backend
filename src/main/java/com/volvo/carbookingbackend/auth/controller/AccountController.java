package com.volvo.carbookingbackend.auth.controller;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.AccountInput;
import com.volvo.carbookingbackend.auth.dto.AuthorityDTO;
import com.volvo.carbookingbackend.auth.entity.Account;
import com.volvo.carbookingbackend.auth.entity.Title;
import com.volvo.carbookingbackend.auth.service.IAccountService;
import com.volvo.carbookingbackend.auth.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final IAccountService accountService;
    private final UserService userService;

    public AccountController(IAccountService accountService,UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping
    public HttpEntity<Page<AccountDTO>> findAll(Pageable pageable,
                                                @RequestParam(required = false) Title title,
                                                @RequestParam(required = false) String keyword){
        return ResponseEntity.ok(accountService.findAll(pageable,title,keyword));
    }

    @GetMapping("/{username}")
    public HttpEntity<AccountDTO> findById(@PathVariable String username){
        return ResponseEntity.ok(accountService.findById(username));
    }

    @GetMapping("/roles")
    public HttpEntity<List<AuthorityDTO>> findAllRoles(){
        return ResponseEntity.ok(accountService.findAllRoles());
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUsername(@PathVariable String username){
        this.userService.deleteUser(username);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateByUsername(@PathVariable String username,@RequestBody AccountInput accountInput) throws InvocationTargetException, IllegalAccessException {
        this.userService.updateUser(username,accountInput);
    }

}
