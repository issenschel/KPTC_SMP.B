package com.example.kptc_smp.exception.email;


public class EmailNotFoundException extends RuntimeException{

    public EmailNotFoundException(){
        super("Почта не найдена");
    }

}
