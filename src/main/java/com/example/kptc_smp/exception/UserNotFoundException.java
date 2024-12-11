package com.example.kptc_smp.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Пользователь не найден");
    }
}
