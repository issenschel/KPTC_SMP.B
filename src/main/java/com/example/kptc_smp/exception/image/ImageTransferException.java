package com.example.kptc_smp.exception.image;

public class ImageTransferException extends RuntimeException {
    public ImageTransferException() {
        super("Перенос фото неудачный");
    }
}
