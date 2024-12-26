package com.example.kptc_smp.dto.guild;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Модель создания заказа")
public class GuildOrderDto {
    @Schema(description = "Заголовок заказа", example = "Что вершит судьбу человечества в этом мире?")
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 50, message = "Заголовок не может быть меньше 3 или больше 50")
    private String header;

    @Schema(description = "Сообщение заказа", example = "Истинному воину меч не нужен...")
    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(min = 8, max = 400, message = "Сообщение не может быть меньше 8 или больше 400")
    private String message;

    @Schema(description = "Псевдоним заказчика", example = "Пасхалко")
    @NotBlank(message = "Псевдоним не может быть пустым")
    @Size(min = 3, max = 30, message = "Пвесдоним не может быть меньше 3 или больше 30")
    private String pseudonym;
}
