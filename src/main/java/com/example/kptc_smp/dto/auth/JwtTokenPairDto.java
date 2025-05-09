package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Токены")
public class JwtTokenPairDto {
    @Schema(description = "Рефреш токены")
    String refreshToken;
    @Schema(description = "Аксес токен")
    String accessToken;
}
