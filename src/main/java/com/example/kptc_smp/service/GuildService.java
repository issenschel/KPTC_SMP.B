package com.example.kptc_smp.service;

import com.example.kptc_smp.dto.guild.ListOrderDto;
import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.Order;
import com.example.kptc_smp.exception.OrderNotFoundException;
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
        orderService.changeOrder(order.orElseThrow(OrderNotFoundException::new), orderDto);
    }

    public void deleteOrder(int id) {
        Optional<Order> order = orderService.findById(id);
        orderService.delete(order.orElseThrow(OrderNotFoundException::new));
    }

    public ListOrderDto getOrders(int page){
        return orderService.getOrders(page);
    }
}
