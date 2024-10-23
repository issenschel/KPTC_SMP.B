package com.example.kptc_smp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginChangeDto {
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    private String password;

    @NotBlank(message = "Новое имя пользователя не может быть пустым")
    @Size(min = 3, max = 30, message = "Новое имя не может быть меньше 3 или больше 30")
    private String newUsername;
}