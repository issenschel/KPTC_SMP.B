package com.example.kptc_smp.exception.profile;

public class CodeValidationException extends RuntimeException {
    public CodeValidationException() {
        super("Неверный код");
    }
}
