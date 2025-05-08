package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "user_information")
public class UserInformation {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "image_registry_id")
    private ImageRegistry imageRegistry;

    @Column(name = "registration_date")
    private LocalDate registrationDate;
}
