package com.example.kptc_smp.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDto {
    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 30, message = "Логин не может быть меньше 3 или больше 30")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше 8 или больше 30")
    private String password;
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 8, max = 30, message = "Подтверждение пароля не может быть меньше 8 или больше 30")
    private String confirmPassword;
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть валидной")
    private String email;
    @NotNull(message = "Код не может быть пустым")
    @Size(max = 6, message = "Код не может быть больше 6")
    private String code;
}
