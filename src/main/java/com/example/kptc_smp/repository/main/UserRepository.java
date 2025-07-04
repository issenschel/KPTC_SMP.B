package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.model.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @EntityGraph(attributePaths = {"userInformation", "actionTickets"})
    Optional<User> findWithInfoAndTicketByUsername(String username);

    @EntityGraph(attributePaths = "userInformation")
    Optional<User> findWithUserInformationByUsername(String username);

    @EntityGraph(attributePaths = {"userSessions"})
    Optional<User> findWithUserSessionsByUsername(String username);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesByUsername(String username);

    Optional<User> findByUsername(String username);
}
