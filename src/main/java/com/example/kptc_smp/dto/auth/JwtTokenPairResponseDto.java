package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Модель пары JWT-токенов доступа и обновления")
public class JwtTokenPairResponseDto {
    @Schema(description = "Рефреш токен")
    String refreshToken;
    @Schema(description = "Аксес токен")
    String accessToken;
}
