package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.EmailVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends CrudRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByEmail(String email);

    void deleteByEmail(String email);
}
