package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "guild_order")
public class GuildOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "header")
    private String header;

    @Column(name = "message")
    private String message;

    @Column(name = "pseudonym")
    private String pseudonym;
}
