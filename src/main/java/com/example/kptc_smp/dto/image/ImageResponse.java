package com.example.kptc_smp.dto.image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Информация о изображении")
@Builder
public class ImageResponse {
    @Schema(description = "UUID изображения")
    private UUID id;
    @Schema(description = "Оригинальное название изображения")
    private String originalName;
    @Schema(description = "Тип изображения")
    private String mimeType;
    @Schema(description = "Размер изображения")
    private Long size;
    @Schema(description = "Дата публикации")
    private LocalDateTime uploadedAt;
    @Schema(description = "Ссылка на изображение")
    private String downloadUrl;
}
