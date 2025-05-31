package com.example.kptc_smp.exception.email;


public class EmailVerificationNotFoundException extends RuntimeException{

    public EmailVerificationNotFoundException(){
        super("Почта для подтверждения кода не найдена");
    }

}
