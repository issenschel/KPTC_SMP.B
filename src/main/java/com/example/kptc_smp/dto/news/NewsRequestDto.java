package com.example.kptc_smp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Модель создания новости")
public class NewsRequestDto {

    @Schema(description = "Заголовок", example = "Крутые новости")
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 200, message = "Заголовок не может быть меньше 3 или больше 200")
    private String title;

    @Schema(description = "Сообщение новости", example = "L или Кира")
    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(min = 8, max = 2000, message = "Сообщение не может быть меньше 8 или больше 2000")
    private String content;
}
