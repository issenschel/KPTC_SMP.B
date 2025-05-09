package com.example.kptc_smp.dto.news;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Модель новости для главной страницы")
@Builder
public class HeadlineNewsDto {
    @Schema(description = "Идентификатор")
    private Integer id;
    @Schema(description = "Заголовок")
    private String title;
    @Schema(description = "Дата выхода новости")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime datePublication;
    @Schema(description = "Ссылка на превью")
    private String previewUrl;
}
