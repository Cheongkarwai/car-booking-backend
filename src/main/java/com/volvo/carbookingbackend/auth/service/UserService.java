package com.volvo.carbookingbackend.auth.service;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.AccountInput;
import com.volvo.carbookingbackend.auth.entity.Account;
import com.volvo.carbookingbackend.auth.repository.AccountRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService , UserDetailsManager {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public UserService(PasswordEncoder passwordEncoder,AccountRepository accountRepository){
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findById(username).orElseThrow();
        return User.withUsername(username).password(user.getPassword()).authorities(user.getAuthorities()
                        .parallelStream().map(e-> new SimpleGrantedAuthority(e.getTitle().name())).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void createUser(UserDetails userDetails) {
        Account user = Account.builder()
                .username(userDetails.getUsername())
                .password(passwordEncoder.encode(userDetails.getPassword()))
                .build();
        this.accountRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
       accountRepository.deleteById(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    public void updateUser(String username, AccountInput accountInput) throws InvocationTargetException, IllegalAccessException {
        Account account = accountRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        account.setPassword(passwordEncoder.encode(accountInput.getPassword()));
        account.setContactNumber(accountInput.getContactNumber());
        account.setFullName(accountInput.getFullName());
        accountRepository.save(account);
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
