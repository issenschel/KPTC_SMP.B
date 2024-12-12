package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.AuthMe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMeRepository extends JpaRepository<AuthMe,Integer> {
}
