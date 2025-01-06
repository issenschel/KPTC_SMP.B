package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.UserDataToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDataTokenRepository extends CrudRepository<UserDataToken, Integer> {
    Optional<UserDataToken> findByTokenUUID(UUID tokenUUID);
}
