package com.example.kptc_smp.exception.email;

public class CodeValidationException extends RuntimeException {
    public CodeValidationException() {
        super("Неверный код");
    }
}
