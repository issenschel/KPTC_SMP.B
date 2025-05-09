package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Модель регистрации")
public class RegistrationUserDto {

    @Schema(description = "Имя пользователя", example = "Keecth_Krut")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 16, message = "Имя пользователя не может быть меньше 3 или больше 16")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Имя пользователя должно содержать только английские буквы,цифры")
    private String username;

    @Schema(description = "Пароль", example = "PassWord!Krut")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$",
            message = "Пароль может содержать только английские буквы и символы без пробелов")
    private String password;

    @Schema(description = "Подтверждение пароля", example = "PassWord!Krut")
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 8, max = 30, message = "Подтверждение пароля не может быть меньше 8 или больше 30")
    private String confirmPassword;

    @Schema(description = "Почта", example = "doingbusiness@gmail.com")
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;

    @Schema(description = "Код", example = "123456")
    @NotNull(message = "Код не может быть пустым")
    @Size(max = 6, message = "Код не может быть больше 6")
    @Pattern(regexp = "^[0-9]+$",
            message = "Код должен содержать только цифры")
    private String code;
}
