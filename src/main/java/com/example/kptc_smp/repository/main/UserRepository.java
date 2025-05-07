package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @EntityGraph(attributePaths = {"userDataToken","userInformation","actionTicket"})
    Optional<User> findWithInfoAndTokenAndTicketByUsername(String username);

    @EntityGraph(attributePaths = "userDataToken")
    Optional<User> findWithUserDataTokenByUsername(String username);

    @EntityGraph(attributePaths = "userInformation")
    Optional<User> findWithUserInformationByUsername(String username);

    @EntityGraph(attributePaths = {"userSessions","userDataToken"})
    Optional<User> findWithSessionsAndTokenByUsername(String username);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesByUsername(String username);

    Optional<User> findByUsername(String username);
}
