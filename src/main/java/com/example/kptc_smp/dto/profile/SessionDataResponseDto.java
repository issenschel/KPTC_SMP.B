package com.example.kptc_smp.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    @Schema(description = "Время входа")
    private Instant entryTime;
}