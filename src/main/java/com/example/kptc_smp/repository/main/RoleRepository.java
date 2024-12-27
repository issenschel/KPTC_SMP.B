package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Byte> {
    Optional<Role> findByName(String name);
}
