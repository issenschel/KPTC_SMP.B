package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Модель получения новостей")
public class HeadlineNewsGroupDto {
    @Schema(description = "Список новостей")
    List<HeadlineNewsDto> news;
    @Schema(description = "Количество страниц с новостями")
    int countPage;
}
