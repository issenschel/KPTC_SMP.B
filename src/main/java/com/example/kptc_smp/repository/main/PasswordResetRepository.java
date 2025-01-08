package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.PasswordReset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByLinkUUID(UUID linkUUID);
}
