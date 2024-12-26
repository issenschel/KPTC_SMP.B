package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Модель новости")
public class NewsResponseDto {
    @Schema(description = "Идентификатор")
    private Integer id;
    @Schema(description = "Заголовок")
    private String title;
    @Schema(description = "Сообщение новости")
    private String content;
    @Schema(description = "Фотография")
    private byte[] photo;
}
