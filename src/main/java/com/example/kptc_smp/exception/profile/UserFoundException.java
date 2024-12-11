package com.example.kptc_smp.exception.profile;

public class UserFoundException extends RuntimeException {
    public UserFoundException() {
        super("Пользователь с таким именем уже существует");
    }
}
