package com.example.kptc_smp.dto.guild;

import com.example.kptc_smp.entity.postgreSQL.GuildOrder;
import lombok.Data;

import java.util.List;

@Data
public class GuildOrdersDto {
    List<GuildOrder> guildOrders;
    int count;
}
