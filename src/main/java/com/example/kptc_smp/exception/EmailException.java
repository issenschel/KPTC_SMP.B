package com.example.kptc_smp.exception;

public class EmailException extends RuntimeException {

    public EmailException() {
        super("Почта уже занята");
    }
}
