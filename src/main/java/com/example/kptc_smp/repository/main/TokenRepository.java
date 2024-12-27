package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByTokenUUID(UUID tokenUUID);
}
