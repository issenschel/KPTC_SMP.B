package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.guild.ListOrderDto;
import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuildService {
    private final OrderService orderService;

    public Order createNewOrder(OrderDto orderDto){
        return orderService.createNewOrder(orderDto);
    }

    public Order changeOrder(OrderDto orderDto, int id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(value -> orderService.changeOrder(value, orderDto)).orElse(null);
    }

    public boolean deleteOrder(int id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            orderService.delete(order.get());
            return true;
        }
        return false;
    }

    public ListOrderDto getOrders(int page){
        return orderService.getOrders(page);
    }
}
