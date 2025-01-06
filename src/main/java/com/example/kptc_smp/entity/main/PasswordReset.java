package com.example.kptc_smp.entity.main;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "password_reset")
public class PasswordReset {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "link_uuid")
    private UUID linkUUID;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public void setExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION);
        this.expiryDate = calendar.getTime();
    }
}
