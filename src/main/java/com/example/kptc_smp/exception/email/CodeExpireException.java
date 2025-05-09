package com.example.kptc_smp.exception.email;

public class CodeExpireException extends RuntimeException{
    public CodeExpireException(){
        super("Время действия кода истекло");
    }
}
