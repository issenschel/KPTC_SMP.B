package com.example.kptc_smp.exception.password;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Новый пароль и подтверждение пароля не совпадают");
    }
}
