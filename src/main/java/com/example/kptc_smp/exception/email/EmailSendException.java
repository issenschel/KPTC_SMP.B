package com.example.kptc_smp.exception.email;

public class EmailSendException extends RuntimeException{
    public EmailSendException(){
        super("Ошибка при отправке письма");
    }
}
