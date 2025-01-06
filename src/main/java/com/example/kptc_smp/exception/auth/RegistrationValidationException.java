package com.example.kptc_smp.exception.auth;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegistrationValidationException extends RuntimeException {
    private final Map<String, String> validationErrors;

    public RegistrationValidationException(Map<String, String> map) {
        super();
        this.validationErrors = map;
    }

}
