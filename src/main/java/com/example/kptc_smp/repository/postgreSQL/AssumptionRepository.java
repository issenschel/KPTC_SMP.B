package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.Assumption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssumptionRepository extends CrudRepository<Assumption, Integer> {
    Optional<Assumption> findByEmail(String email);
}
