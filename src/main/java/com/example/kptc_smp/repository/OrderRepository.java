package com.example.kptc_smp.repository;

import com.example.kptc_smp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}