package com.example.kptc_smp.exception.image;

public class OldImageNotFoundException extends RuntimeException {
    public OldImageNotFoundException() {
        super("Старое фото не найдено");
    }
}
