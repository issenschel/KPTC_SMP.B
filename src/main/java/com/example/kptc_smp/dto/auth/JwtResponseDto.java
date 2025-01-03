package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения токена с ролью")
public class JwtResponseDto {
    @Schema(description = "Токен")
    private String token;
    @Schema(description = "Список ролей")
    private List<String> role;
}
