package com.example.kptc_smp.repositories;

import com.example.kptc_smp.entitys.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
