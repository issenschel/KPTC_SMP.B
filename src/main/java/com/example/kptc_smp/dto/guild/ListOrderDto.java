package com.example.kptc_smp.dto.guild;

import com.example.kptc_smp.entity.postgreSQL.Order;
import lombok.Data;

import java.util.List;

@Data
public class ListOrderDto {
    List<Order> orders;
    int count;
}
