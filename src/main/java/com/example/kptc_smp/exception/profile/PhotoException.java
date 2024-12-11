package com.example.kptc_smp.exception.profile;

public class PhotoException extends RuntimeException {
    public PhotoException() {
        super("Что-то не так с фото");
    }

}
