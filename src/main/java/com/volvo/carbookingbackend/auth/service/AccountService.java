package com.volvo.carbookingbackend.auth.service;

import com.volvo.carbookingbackend.appointment.specification.AppointmentSpecification;
import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.AuthorityDTO;
import com.volvo.carbookingbackend.auth.entity.Account;
import com.volvo.carbookingbackend.auth.entity.Authority;
import com.volvo.carbookingbackend.auth.entity.Title;
import com.volvo.carbookingbackend.auth.repository.AccountRepository;
import com.volvo.carbookingbackend.auth.repository.AuthorityRepository;
import com.volvo.carbookingbackend.auth.specification.AccountSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    private final AuthorityRepository authorityRepository;

    private final Function<Account, AccountDTO> accountDTOFunction = account -> AccountDTO.builder().username(account.getUsername())
            .fullName(account.getFullName())
            .contactNumber(account.getContactNumber()).build();

    private final Function<Authority, AuthorityDTO> authorityDTOFunction = authority ->
            AuthorityDTO.builder().id(authority.getId()).title(authority.getTitle().name()).build();

    public AccountService(AccountRepository accountRepository, AuthorityRepository authorityRepository) {
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Page<AccountDTO> findAll(Pageable pageable, Title title, String keyword) {

        Specification<Account> accountSpecification = Specification.where(null);

        if (Objects.nonNull(title)) {
            accountSpecification = accountSpecification.and(AccountSpecification.hasTitle(title));
        }

        if (StringUtils.hasText(keyword)) {
            accountSpecification = accountSpecification.and(AccountSpecification.hasKeyword(keyword));
        }

        return accountRepository.findAll(accountSpecification, Objects.requireNonNullElse(pageable,
                        Pageable.ofSize(Integer.MAX_VALUE)))
                .map(accountDTOFunction);

    }

    @Override
    public AccountDTO findById(String username) {
        return this.accountDTOFunction.apply(this.accountRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public List<AuthorityDTO> findAllRoles() {
        return authorityRepository.findAll().parallelStream().map(authorityDTOFunction).collect(Collectors.toList());
    }


}
