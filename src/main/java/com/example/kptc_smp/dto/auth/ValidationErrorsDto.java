package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Schema(description = "Модель ошибок при регистрации")
public class ValidationErrorsDto {
    @Schema(description = "Список ошибок")
    private Map<String, String> errors;
}
