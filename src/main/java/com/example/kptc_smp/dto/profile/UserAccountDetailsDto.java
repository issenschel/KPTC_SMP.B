package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения данных об аккаунте пользователя")
public class UserAccountDetailsDto {
    @Schema(description = "Идентификатор", example = "1")
    private Integer id;
    @Schema(description = "Имя пользователя", example = "Keecth_Krut")
    private String username;
    @Schema(description = "Почта", example = "doingbusiness@gmail.com")
    private String email;
    @Schema(description = "Дата регистрации", example = "2025-01-01")
    private LocalDate registrationDate;

}
