package com.example.kptc_smp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorsDto {
    private Map<String, String> errors;
}
