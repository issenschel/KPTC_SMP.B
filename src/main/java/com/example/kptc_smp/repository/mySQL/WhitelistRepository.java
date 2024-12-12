package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhitelistRepository extends JpaRepository<Whitelist, Integer> {
}
