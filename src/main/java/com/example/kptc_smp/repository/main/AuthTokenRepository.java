package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.AuthToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, Integer> {
    Optional<AuthToken> findByTokenUUID(UUID tokenUUID);
}
