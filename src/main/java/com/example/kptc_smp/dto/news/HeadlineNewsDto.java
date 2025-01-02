package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Модель новости для главной страницы")
public class HeadlineNewsDto {
    @Schema(description = "Идентификатор")
    private Integer id;
    @Schema(description = "Заголовок")
    private String title;
    @Schema(description = "Дата выхода новости")
    private LocalDate datePublication;
    @Schema(description = "Имя изображения")
    private String photoName;
}
