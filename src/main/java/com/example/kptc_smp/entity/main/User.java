package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "auth_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    private UserInformation userInformation;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    private UserDataToken userDataToken;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
    private PasswordReset passwordReset;
}