package com.example.kptc_smp.repository.mySQL;

import com.example.kptc_smp.entity.mySQL.ExternalWhitelist;
import org.springframework.data.repository.CrudRepository;

public interface WhitelistRepository extends CrudRepository<ExternalWhitelist, Integer> {
}
