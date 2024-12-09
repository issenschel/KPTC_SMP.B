package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.guild.ListOrderDto;
import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.Order;
import com.example.kptc_smp.exception.OrderChangeException;
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

    public void changeOrder(OrderDto orderDto, int id) {
        Optional<Order> order = orderService.findById(id);
        orderService.changeOrder(order.orElseThrow(OrderChangeException::new), orderDto);
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
