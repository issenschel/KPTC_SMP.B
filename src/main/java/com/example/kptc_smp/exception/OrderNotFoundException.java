package com.example.kptc_smp.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Объявление не найдено");
    }
}
