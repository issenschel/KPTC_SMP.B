package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "user_data_token")
public class UserDataToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "token_uuid")
    private UUID tokenUUID;

}
