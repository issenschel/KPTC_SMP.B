package com.example.kptc_smp.service;


import com.example.kptc_smp.dto.guild.ListOrderDto;
import com.example.kptc_smp.dto.guild.OrderDto;
import com.example.kptc_smp.entity.Order;
import com.example.kptc_smp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order createNewOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setHeader(orderDto.getHeader());
        order.setMessage(orderDto.getMessage());
        order.setPseudonym(orderDto.getPseudonym());
        return orderRepository.save(order);
    }

    public ListOrderDto getOrders(int page) {
        ListOrderDto listOrderDto = new ListOrderDto();
        PageRequest pageRequest = PageRequest.of(page, 6);
        Page<Order> ordersPage = orderRepository.findAll(pageRequest);
        int totalPages = ordersPage.getTotalPages();
        listOrderDto.setOrders(ordersPage.getContent());
        listOrderDto.setCount(totalPages);
        return listOrderDto;
    }

    @Transactional
    public void changeOrder(Order order,OrderDto orderDto){
        order.setHeader(orderDto.getHeader());
        order.setMessage(orderDto.getMessage());
        order.setPseudonym(orderDto.getPseudonym());
        orderRepository.save(order);
    }

    public void delete(Order order){
        orderRepository.delete(order);
    }

    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }
}
