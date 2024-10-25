package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.OrderDto;
import com.example.kptc_smp.entitys.Order;
import com.example.kptc_smp.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createNewOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setHeader(orderDto.getHeader());
        order.setMessage(orderDto.getMessage());
        order.setPseudonym(orderDto.getPseudonym());
        return orderRepository.save(order);
    }

    public List<Order> getOrders(int page) {
        PageRequest pageRequest = PageRequest.of(page, 6);
        Page<Order> ordersPage = orderRepository.findAll(pageRequest);
        return ordersPage.getContent();
    }

}
