package com.example.kptc_smp.exception.image;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super("Фото не найдено");
    }
}
