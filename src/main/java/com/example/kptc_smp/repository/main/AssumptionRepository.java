package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.Assumption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssumptionRepository extends CrudRepository<Assumption, Integer> {
    Optional<Assumption> findByEmail(String email);

    void deleteByEmail(String email);
}
