package com.example.kptc_smp.dto.guild;

import com.example.kptc_smp.entity.main.GuildOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Модель получения списка заказов")
public class GuildOrderGroupDto {
    @Schema(description = "Список заказов")
    List<GuildOrder> guildOrders;
    @Schema(description = "Количество страниц с заказами")
    int countPage;
}
