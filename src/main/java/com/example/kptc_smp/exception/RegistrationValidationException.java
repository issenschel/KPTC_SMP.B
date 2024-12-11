package com.example.kptc_smp.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegistrationValidationException extends RuntimeException {
    private final Map<String, String> validationErrors;

    public RegistrationValidationException(Map<String, String> map) {
        super("Validation Failed");
        this.validationErrors = map;
    }

}
