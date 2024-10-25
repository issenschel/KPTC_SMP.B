package com.example.kptc_smp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderDto {
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 30, message = "Заголовок не может быть меньше 3 или больше 30")
    private String header;

    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(min = 8, max = 150, message = "Сообщение не может быть меньше 3 или больше 150")
    private String message;

    @NotBlank(message = "Псевдоним не может быть пустым")
    @Size(min = 3, max = 30, message = "Пвесдоним не может быть меньше 3 или больше 30")
    private String pseudonym;
}
