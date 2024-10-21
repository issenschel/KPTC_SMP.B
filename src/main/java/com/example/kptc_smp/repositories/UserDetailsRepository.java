package com.example.kptc_smp.repositories;

import com.example.kptc_smp.entitys.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
    Optional<UserDetails> findByEmail(String email);
    Optional<UserDetails> findByMinecraftName(String minecraftName);

}
