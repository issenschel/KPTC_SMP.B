package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.model.main.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Byte> {
    Optional<Role> findByName(String name);
}
