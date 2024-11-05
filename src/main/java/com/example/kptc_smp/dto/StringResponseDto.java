package com.example.kptc_smp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class StringResponseDto {
    private String message;
    private HttpStatus status;
}
