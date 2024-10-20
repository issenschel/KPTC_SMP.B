package com.example.kptc_smp.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long  id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "email")
    private String email;

    @Column(name = "minecraftname")
    private String minecraftName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "versionid")
    private String versionId;

}
