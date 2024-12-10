package com.example.kptc_smp.exception;

public class UserNotFountException extends RuntimeException {

    public UserNotFountException() {
        super("Пользователь не найден");
    }
}
