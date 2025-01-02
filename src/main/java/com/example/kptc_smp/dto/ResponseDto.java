package com.example.kptc_smp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Модель сообщения")
public class ResponseDto {
    private String message;
}
