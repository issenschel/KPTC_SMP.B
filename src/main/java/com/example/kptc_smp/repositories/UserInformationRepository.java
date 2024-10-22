package com.example.kptc_smp.repositories;

import com.example.kptc_smp.entitys.UserInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends CrudRepository<UserInformation, Long> {
    Optional<UserInformation> findByEmail(String email);
    Optional<UserInformation> findByMinecraftName(String minecraftName);

}
