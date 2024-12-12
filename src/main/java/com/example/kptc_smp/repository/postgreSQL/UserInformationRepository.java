package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.UserInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends CrudRepository<UserInformation, Integer> {
    Optional<UserInformation> findByEmail(String email);
    Optional<UserInformation> findByMinecraftName(String minecraftName);
}
