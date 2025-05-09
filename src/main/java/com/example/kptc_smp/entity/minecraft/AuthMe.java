package com.example.kptc_smp.entity.minecraft;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authme")
public class AuthMe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "realname")
    private String realName;

    @Column(name = "password")
    private String password;

    @Column(name = "ip")
    private String ip;

    @Column(name = "lastlogin")
    private Long lastLogin;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "z")
    private Double z;

    @Column(name = "world")
    private String world;

    @Column(name = "regdate")
    private Long registrationDate;

    @Column(name = "regip")
    private String regIP;

    @Column(name = "yaw")
    private Float yaw;

    @Column(name = "pitch")
    private Float pitch;

    @Column(name = "email")
    private String email;

    @Column(name = "isLogged")
    private boolean isLogged;

    @Column(name = "hasSession")
    private boolean hasSession;

    @Column(name = "totp")
    private String totp;
}
