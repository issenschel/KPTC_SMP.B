package com.example.kptc_smp.exception.jwt;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
        super("JWT токен устарел");
    }

}
