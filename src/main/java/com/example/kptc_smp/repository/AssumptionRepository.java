package com.example.kptc_smp.repository;

import com.example.kptc_smp.entity.Assumption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssumptionRepository extends CrudRepository<Assumption, Long> {
    Optional<Assumption> findByEmail(String email);
}
