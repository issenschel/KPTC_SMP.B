package com.example.kptc_smp.exception;

import java.io.IOException;

public class PhotoException extends RuntimeException {
    public PhotoException() {
        super("Что-то не так с фото");
    }

}
