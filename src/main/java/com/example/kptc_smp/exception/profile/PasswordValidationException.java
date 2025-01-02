package com.example.kptc_smp.exception.profile;

public class PasswordValidationException extends RuntimeException {

    public PasswordValidationException() {
        super("Неверный текущий пароль");
    }

    public PasswordValidationException(String message) {
        super(message);
    }
}
