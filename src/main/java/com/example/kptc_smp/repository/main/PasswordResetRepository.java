package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.PasswordReset;
import com.example.kptc_smp.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Integer> {

    @EntityGraph(attributePaths = {"user"},type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT p FROM PasswordReset p WHERE p.linkUUID = :linkUUID")
    Optional<PasswordReset> findByLinkUUID(UUID linkUUID);

    Optional<PasswordReset> findByUser(User user);
}

