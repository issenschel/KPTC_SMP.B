package com.example.kptc_smp.exception.auth;

public class PasswordResetDateExpiredException extends RuntimeException {
    public PasswordResetDateExpiredException() {
        super("Время для смены пароля истекло");
    }
}
