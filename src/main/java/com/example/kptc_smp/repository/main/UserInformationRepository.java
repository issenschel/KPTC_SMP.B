package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.UserInformation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInformationRepository extends CrudRepository<UserInformation, Integer> {

    Optional<UserInformation> findByEmail(String email);

    @EntityGraph(attributePaths = "user")
    @Query("SELECT u FROM UserInformation u WHERE u.email = :email")
    Optional<UserInformation> findWithUserByEmail(String email);
}
