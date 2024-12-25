package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenVersionRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByTokenUUID(UUID tokenUUID);
}
