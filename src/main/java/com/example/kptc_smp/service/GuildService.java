package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.ListOrderDto;
import com.example.kptc_smp.dto.OrderDto;
import com.example.kptc_smp.entitys.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuildService {
    private final OrderService orderService;

    public ResponseEntity<?> createNewOrder(OrderDto orderDto){
        Order order = orderService.createNewOrder(orderDto);
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<?> changeOrder(OrderDto orderDto, int id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            Order changeOrder = orderService.changeOrder(order.get(), orderDto);
            return ResponseEntity.ok(changeOrder);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Заказ не найден");
    }

    public ResponseEntity<?> deleteOrder(int id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            orderService.delete(order.get());
            return ResponseEntity.ok().body("Заказ удален");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Заказ не найден");
    }

    public ListOrderDto getOrders(int page){
        return orderService.getOrders(page);
    }
}
