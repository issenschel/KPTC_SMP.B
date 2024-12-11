package com.example.kptc_smp.exception.profile;

public class PasswordValidationException extends RuntimeException {
    public PasswordValidationException() {
        super("Пароли не совпадают");
    }
}
