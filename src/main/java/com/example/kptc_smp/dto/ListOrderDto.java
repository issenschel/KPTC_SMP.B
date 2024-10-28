package com.example.kptc_smp.dto;

import com.example.kptc_smp.entitys.Order;
import lombok.Data;

import java.util.List;

@Data
public class ListOrderDto {
    List<Order> orders;
    int count;
}
