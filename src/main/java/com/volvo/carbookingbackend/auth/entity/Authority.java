package com.volvo.carbookingbackend.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_id_generator")
    @SequenceGenerator(name = "role_id_generator",sequenceName = "tbl_role_seq",allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Title title;

    @ManyToMany(mappedBy = "authorities")
    private Set<Account> accounts = new HashSet<>();
}
