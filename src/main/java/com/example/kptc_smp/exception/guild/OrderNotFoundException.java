package com.example.kptc_smp.exception.guild;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Объявление не найдено");
    }
}
