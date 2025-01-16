package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "password_reset")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "link_uuid")
    private UUID linkUUID;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
