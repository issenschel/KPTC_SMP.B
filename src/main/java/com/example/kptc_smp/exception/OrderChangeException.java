package com.example.kptc_smp.exception;

public class OrderChangeException extends RuntimeException {

    public OrderChangeException() {
        super("Объявление не найдено");
    }
}
