package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.GuildOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildOrderRepository extends JpaRepository<GuildOrder, Integer> {

}
