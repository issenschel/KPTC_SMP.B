package com.example.kptc_smp.repositories;

import com.example.kptc_smp.entitys.Assumption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssumptionRepository extends CrudRepository<Assumption, Long> {
    Optional<Assumption> findByEmail(String email);
}
