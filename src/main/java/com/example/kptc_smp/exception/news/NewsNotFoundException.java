package com.example.kptc_smp.exception.news;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {
        super("Новость не найдена");
    }
}
