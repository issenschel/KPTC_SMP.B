package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Модель получения токенов с ролью")
public class AuthResponseDto {
    @Schema(description = "Модель токенов")
    private JwtTokenPairDto jwtTokenPairDto;
    @Schema(description = "Список ролей")
    private List<String> roles;
}
