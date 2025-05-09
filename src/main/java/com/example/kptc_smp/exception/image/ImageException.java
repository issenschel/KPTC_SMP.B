package com.example.kptc_smp.exception.image;

public class ImageException extends RuntimeException {
    public ImageException() {
        super("При обработке изображения что-то пошло не так");
    }

    public ImageException(String message) {
        super(message);
    }


}
