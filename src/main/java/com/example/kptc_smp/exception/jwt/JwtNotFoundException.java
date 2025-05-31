package com.example.kptc_smp.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtNotFoundException extends RuntimeException {

    public JwtNotFoundException() {
        super("Токен не найден");
    }
}
