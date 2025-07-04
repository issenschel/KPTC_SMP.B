package com.example.kptc_smp.exception.jwt;

public class JwtNotFoundException extends RuntimeException {

    public JwtNotFoundException() {
        super("Токен не найден");
    }
}
