package com.example.kptc_smp.exception.role;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(){
        super("Роль не найдена");
    }
}
