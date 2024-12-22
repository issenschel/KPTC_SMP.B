package com.example.kptc_smp.dto.registration;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationUserDto {
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

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 8, max = 30, message = "Подтверждение пароля не может быть меньше 8 или больше 30")
    private String confirmPassword;

    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;

    @NotNull(message = "Код не может быть пустым")
    @Size(max = 6, message = "Код не может быть больше 6")
    @Pattern(regexp = "^[0-9]+$",
            message = "Код должен содержать только цифры")
    private String code;
}
