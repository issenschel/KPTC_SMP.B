package com.example.kptc_smp.repository;

import com.example.kptc_smp.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles","userInformation","tokenVersion"})
    Optional<User> findByUsername(String username);
}
