package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.ExternalAuthMe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthMeRepository extends CrudRepository<ExternalAuthMe,Integer> {
    Optional<ExternalAuthMe> findByUsername(String username);
}
