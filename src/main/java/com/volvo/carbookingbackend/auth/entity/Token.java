package com.volvo.carbookingbackend.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "token_id_generator")
    @SequenceGenerator(name = "token_id_generator",sequenceName = "tbl_token_seq",allocationSize = 1)
    private Long id;

    @Column(name="access_token",nullable = false,columnDefinition = "text")
    private String accessToken;

    @Column(name="refresh_token",columnDefinition = "text")
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_username")
    private Account account;
}
