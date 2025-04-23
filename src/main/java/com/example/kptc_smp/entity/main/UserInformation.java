package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_information")
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer  id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "email")
    private String email;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "registration_date")
    private LocalDate registrationDate;
}
