package com.example.kptc_smp.exception.user;

public class ActiveSessionDeletionException extends RuntimeException {
    public ActiveSessionDeletionException(){
        super("Нельзя удалить текущую сессию");
    }
}
