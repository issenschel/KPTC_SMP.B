package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.guild.ListOrderDto;
import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.postgreSQL.Order;
import com.example.kptc_smp.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildService {
    private final OrderService orderService;

    public Order createNewOrder(OrderDto orderDto) {
        return orderService.createNewOrder(orderDto);
    }

    public Order changeOrder(OrderDto orderDto, int id) {
        Order order = orderService.findById(id).orElseThrow(OrderNotFoundException::new);
        orderService.changeOrder(order, orderDto);
        return order;
    }

    public void deleteOrder(int id) {
        Order order = orderService.findById(id).orElseThrow(OrderNotFoundException::new);
        orderService.delete(order);
    }

    public ListOrderDto getOrders(int page) {
        return orderService.getOrders(page);
    }
}
