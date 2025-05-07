package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "action_ticket")
public class ActionTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ticket")
    private String ticket;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
