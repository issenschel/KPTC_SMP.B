package com.example.kptc_smp.exception.email;

public class EmailFoundException extends RuntimeException {

    public EmailFoundException() {
        super("Почта уже занята");
    }
}
