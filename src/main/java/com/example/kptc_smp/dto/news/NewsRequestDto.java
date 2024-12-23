package com.example.kptc_smp.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsRequestDto {
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(max = 200, message = "Заголовок не может быть меньше 3 или больше 30")
    private String title;

    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(max = 2000, message = "Сообщение не может быть меньше 8 или больше 150")
    private String content;
}
