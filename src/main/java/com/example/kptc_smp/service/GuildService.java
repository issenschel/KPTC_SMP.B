package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.OrderDto;
import com.example.kptc_smp.entitys.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuildService {
    private final OrderService orderService;

    public ResponseEntity<?> createNewOrder(OrderDto orderDto){
        Order order = orderService.createNewOrder(orderDto);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<?> getOrders(int page){
        List<Order> orders = orderService.getOrders(page);
        return ResponseEntity.ok(orders);
    }
}
