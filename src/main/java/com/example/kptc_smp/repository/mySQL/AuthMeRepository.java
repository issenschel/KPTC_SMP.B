package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.AuthMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthMeRepository extends CrudRepository<AuthMe,Integer> {
    Optional<AuthMe> findByUsername(String username);
}
