package com.volvo.carbookingbackend.auth.service;

import com.volvo.carbookingbackend.auth.dto.AccountDTO;
import com.volvo.carbookingbackend.auth.dto.TokenDTO;
import com.volvo.carbookingbackend.auth.entity.Account;
import com.volvo.carbookingbackend.auth.entity.Token;
import com.volvo.carbookingbackend.auth.repository.AccountRepository;
import com.volvo.carbookingbackend.auth.repository.TokenRepository;
import com.volvo.carbookingbackend.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class JwtTokenService implements TokenService{

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AccountRepository userRepository;
    private final TokenRepository tokenRepository;

    @Value("${jwt.issuer}")
    private String issuer;


    public JwtTokenService(JwtEncoder jwtEncoder,
                           JwtDecoder jwtDecoder,
                           AccountRepository userRepository,
                           TokenRepository tokenRepository){
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public TokenDTO generateToken(UserDetails userDetails) {

        String accessToken = this.generateAccessToken(userDetails);
        String refreshToken = this.generateRefreshToken(userDetails);

        tokenRepository.findByAccountUsername(userDetails.getUsername())
                .ifPresentOrElse(token -> {
                    token.setAccessToken(accessToken);
                    token.setRefreshToken(refreshToken);
                    tokenRepository.save(token);
                },()->{
                    Account user = userRepository.findById(userDetails.getUsername())
                            .orElseThrow(()-> new UsernameNotFoundException("User not found"));
                    Token token = Token.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                    user.setToken(token);
                    userRepository.save(user);
                });
        return TokenDTO.builder()
                .accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public TokenDTO validateRefreshToken(String refreshToken) {
        Jwt jwt = jwtDecoder.decode(refreshToken);
        Account user = userRepository.findById(jwt.getSubject())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(!Objects.equals(user.getToken().getRefreshToken(),refreshToken)){
            throw new InvalidTokenException("Refresh token is invalid");
        }

        return this.generateToken(org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities().parallelStream().map(e-> new SimpleGrantedAuthority(e.getTitle().name())).collect(Collectors.toSet())).build());
    }

    public boolean validateTokenValidity(String accessToken){

        try{
            jwtDecoder.decode(accessToken);
        }
        catch (JwtException e){
            return false;
        }

        return true;


    }

    @Override
    public AccountDTO decodeJwt(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);
        List<String> authorities = jwt.getClaimAsStringList("authorities");

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAuthorities(authorities.parallelStream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        accountDTO.setUsername(jwt.getSubject());
        return accountDTO;
    }

    private String generateAccessToken(UserDetails userDetails){
        JwsHeader jwsHeader = JwsHeader.with(() -> JwsAlgorithms.RS256).build();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .claim("authorities",userDetails.getAuthorities().stream().parallel().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .issuer(issuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.MINUTES)).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
    }

    private String generateRefreshToken(UserDetails userDetails) {
        JwsHeader jwsHeader = JwsHeader.with(() -> JwsAlgorithms.RS256).build();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .claim("authorities",userDetails.getAuthorities().stream().parallel().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .issuer(issuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS)).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
    }
}