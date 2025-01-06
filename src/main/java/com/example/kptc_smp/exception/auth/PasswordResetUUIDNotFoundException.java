package com.example.kptc_smp.exception.auth;

public class PasswordResetUUIDNotFoundException extends RuntimeException {
    public PasswordResetUUIDNotFoundException() {
        super("UUID не найден");
    }
}
