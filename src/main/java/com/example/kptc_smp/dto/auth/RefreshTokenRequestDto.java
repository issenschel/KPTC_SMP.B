package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Модель обновления токена")
public class RefreshTokenRequestDto {
    @Schema(description = "Рефреш токен")
    private String refreshToken;
}
