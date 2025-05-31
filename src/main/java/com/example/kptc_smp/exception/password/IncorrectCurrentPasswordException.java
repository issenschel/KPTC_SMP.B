package com.example.kptc_smp.exception.password;

public class IncorrectCurrentPasswordException extends RuntimeException{
    public IncorrectCurrentPasswordException() {
        super("Неверный текущий пароль");
    }
}
