package com.example.kptc_smp.exception.auth;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super("Токен не найден");
    }
}
