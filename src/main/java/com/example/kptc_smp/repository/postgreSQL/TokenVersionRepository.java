package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.TokenVersion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenVersionRepository extends CrudRepository<TokenVersion, Integer> {
    @Query("SELECT v.version FROM TokenVersion v WHERE v.version = :token")
    Optional<TokenVersion> findByVersion(String token);
}
