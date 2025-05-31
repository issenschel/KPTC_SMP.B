package com.example.kptc_smp.repository.minecraft;

import com.example.kptc_smp.model.minecraft.AuthMe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthMeRepository extends CrudRepository<AuthMe,Integer> {
    Optional<AuthMe> findByUsername(String username);
}
