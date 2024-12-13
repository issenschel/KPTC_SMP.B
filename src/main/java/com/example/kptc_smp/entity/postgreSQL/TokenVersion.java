package com.example.kptc_smp.entity.postgreSQL;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tokens_version")
public class TokenVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "version")
    private String version;

}
