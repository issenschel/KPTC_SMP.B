package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface WhitelistRepository extends CrudRepository<Whitelist, Integer> {
}
