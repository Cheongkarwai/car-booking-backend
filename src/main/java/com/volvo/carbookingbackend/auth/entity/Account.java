package com.volvo.carbookingbackend.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "tbl_account")
public class Account {

    @Id
    private String username;

    @Column(columnDefinition = "text")
    private String password;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="tbl_account_authority",
            joinColumns = {@JoinColumn(name = "account_username")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")}
    )
    private Set<Authority> authorities = new HashSet<>();

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("contact_number")
    private String contactNumber;

    @OneToOne(mappedBy = "account",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
    private Token token;

    public void setToken(Token token){
        token.setAccount(this);
        this.token = token;
    }
}
