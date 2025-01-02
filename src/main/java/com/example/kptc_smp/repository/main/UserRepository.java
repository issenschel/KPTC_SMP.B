package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @EntityGraph(attributePaths = {"roles","userInformation","token"})
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findWithAllDependenciesByUsername(String username);

    @EntityGraph(attributePaths = {"token","userInformation"})
    Optional<User> findWithUserInformationAndTokenVersionByUsername(String username);

    @EntityGraph(attributePaths = "token")
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findWithTokenVersionByUsername(String username);

    @EntityGraph(attributePaths = "userInformation")
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findWithUserInformationByUsername(String username);

    Optional<User> findByUsername(String username);
}
