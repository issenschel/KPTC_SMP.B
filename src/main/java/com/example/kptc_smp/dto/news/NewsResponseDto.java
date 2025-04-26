package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Модель новости")
public class NewsResponseDto {
    @Schema(description = "Идентификатор")
    private Integer id;
    @Schema(description = "Заголовок")
    private String title;
    @Schema(description = "Текст новости")
    private String content;
    @Schema(description = "Дата выхода новости")
    private LocalDateTime datePublication;
    @Schema(description = "Ссылка на изображение")
    private String photoUrl;
}
