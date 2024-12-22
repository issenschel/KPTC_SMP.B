package com.example.kptc_smp.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestDto {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 30, message = "Логин не может быть меньше 3 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "Логин должен содержать только английские буквы и цифры")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
            message = "Пароль может содержать только английские буквы и символы без пробелов")
    private String password;
}
