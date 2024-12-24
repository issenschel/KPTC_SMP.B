package com.example.kptc_smp.exception;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {
        super("Новость не найдена");
    }
}
