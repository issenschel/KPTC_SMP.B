package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Модель входа в аккаунт")
public class AuthRequestDto {

    @Schema(description = "Имя пользователя", example = "bokuNo_Pico")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 16, message = "Имя пользователя не может быть меньше 3 или больше 16")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Имя пользователя должно содержать только английские буквы,цифры и знак '_' ")
    private String username;

    @Schema(description = "Пароль", example = "old_Miller@$^!")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
            message = "Пароль может содержать только английские буквы и символы без пробелов")
    private String password;
}
