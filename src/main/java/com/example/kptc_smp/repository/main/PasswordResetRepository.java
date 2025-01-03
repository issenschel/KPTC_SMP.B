package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.PasswordReset;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);
}
