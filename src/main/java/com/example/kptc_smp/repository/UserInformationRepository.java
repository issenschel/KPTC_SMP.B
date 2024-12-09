package com.example.kptc_smp.repository;

import com.example.kptc_smp.entity.UserInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends CrudRepository<UserInformation, Long> {
    Optional<UserInformation> findByEmail(String email);
    Optional<UserInformation> findByMinecraftName(String minecraftName);
}
