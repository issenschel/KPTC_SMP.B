package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
