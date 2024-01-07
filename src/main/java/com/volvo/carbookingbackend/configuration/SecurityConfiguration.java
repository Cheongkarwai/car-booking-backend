package com.volvo.carbookingbackend.configuration;


import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${jwt.key.location.public}")
    private RSAPublicKey rsaPublicKey;

    @Value("${jwt.key.location.private}")
    private RSAPrivateKey rsaPrivateKey;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {

        httpSecurity
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequestConfigurer->httpRequestConfigurer
                        .requestMatchers(HttpMethod.GET,"/api/v1/cars/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/appointments").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/appointments/**","/api/v1/cars/**","/api/v1/accounts/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServerConfigurer->oauth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.decoder(this.accessTokenDecoder())
                        .jwtAuthenticationConverter(this.jwtAuthenticationConverter())))
                .sessionManagement(sessionManagementConfigurer->sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer->exceptionHandlingConfigurer.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()));

        return httpSecurity.getOrBuild();
    }

    @Bean
    public JwtDecoder accessTokenDecoder(){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).signatureAlgorithm(SignatureAlgorithm.RS256).build();
        jwtDecoder.setJwtValidator(tokenValidator());
        return jwtDecoder;
    }

    public OAuth2TokenValidator<Jwt> tokenValidator(){
        List<OAuth2TokenValidator<Jwt>> oAuth2TokenValidators = List.of(
                new JwtTimestampValidator(),
                new JwtIssuerValidator("cheong")
        );
        return new DelegatingOAuth2TokenValidator<>(oAuth2TokenValidators);
    }

    @Bean
    public JwtEncoder accessTokenEncoder(){
        JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        JWKSet jwkSet = new JWKSet(jwk);
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200","http://"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),HttpMethod.POST.name(),
                HttpMethod.OPTIONS.name(),HttpMethod.DELETE.name(),HttpMethod.PUT.name(),HttpMethod.PATCH.name()));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }


//    //Allow spring security to ignore certain unprotected route
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(HttpMethod.GET,"/api/v1/cars/**")
//                .requestMatchers(HttpMethod.POST,"/api/v1/appointments")
//                .requestMatchers("/api/v1/auth/**");
//    }

}

class RequestMatcherImpl implements  RequestMatcher{

    @Override
    public boolean matches(HttpServletRequest request) {

        if(request.getRequestURI().equals("/api/v1/cars")){
            return  request.getMethod().equals(HttpMethod.GET.name());
        }
        if(request.getRequestURI().equals("/api/v1/appointments")){
            return request.getMethod().equals(HttpMethod.POST.name());
        }
        return false;
    }

    @Override
    public MatchResult matcher(HttpServletRequest request) {
        return RequestMatcher.super.matcher(request);
    }
}

