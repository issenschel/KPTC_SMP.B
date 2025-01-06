package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "email_verification")
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;
}
