package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Модель получения новостей")
public class ListNewsDto {
    @Schema(description = "Список новостей")
    List<NewsResponseDto> news;
    @Schema(description = "Количество страниц с новостями")
    int count;
}
