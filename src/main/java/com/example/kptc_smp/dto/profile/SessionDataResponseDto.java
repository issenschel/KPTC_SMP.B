package com.example.kptc_smp.dto.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Модель данных сессий")
public class SessionDataResponseDto {
    @Schema(description = "id сессии")
    private UUID id;
    @Schema(description = "ip адрес")
    private String ipAddress;
    @Schema(description = "Информация об устройстве")
    private Map<String, String> userAgent;
}