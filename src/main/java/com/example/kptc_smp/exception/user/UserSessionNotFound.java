package com.example.kptc_smp.exception.user;

public class UserSessionNotFound extends RuntimeException{
    public UserSessionNotFound(){
        super("Сессия пользователя не найдена");
    }
}
