package com.example.kptc_smp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Модель обновления токена")
public class RefreshTokenRequestDto {
    @Schema(description = "Рефреш токен", example = "abobababaf24214125125qasrq")
    @NotBlank(message = "Рефреш токен не может быть пустым")
    private String refreshToken;
}
