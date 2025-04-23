package com.example.kptc_smp.exception.image;

public class ImageInvalidFormatException extends RuntimeException {
    public ImageInvalidFormatException() {
        super("Формат фото не соответствует требованиям");
    }
}
