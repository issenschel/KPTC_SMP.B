package com.example.kptc_smp.entity.postgreSQL;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "assumptions")
public class Assumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;
}
