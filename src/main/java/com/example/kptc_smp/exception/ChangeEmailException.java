package com.example.kptc_smp.exception;

public class ChangeEmailException extends RuntimeException {

    public ChangeEmailException() {
        super("Почта уже занята");
    }
}
