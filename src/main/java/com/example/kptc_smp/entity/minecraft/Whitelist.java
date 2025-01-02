package com.example.kptc_smp.entity.minecraft;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "mysql_whitelist")
public class Whitelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "UUID")
    private String minecraftUUID;

    @Column(name = "user")
    private String user;
}
