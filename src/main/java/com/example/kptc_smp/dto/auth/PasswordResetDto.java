package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Модель смены пароля")
public class PasswordResetDto {
    @Schema(description = "Новый пароль", example = "Desiigner_^@")
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
            message = "Новый пароль может содержать только английские буквы и символы без пробелов")
    private String password;

    @Schema(description = "Подтверждение пароля пароль", example = "Desiigner_^@")
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 8, max = 30, message = "Подтверждение пароля не может быть меньше 8 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
            message = "Подтверждение пароля может содержать только английские буквы и символы без пробелов")
    private String confirmPassword;
}
