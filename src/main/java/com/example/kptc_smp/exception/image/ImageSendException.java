package com.example.kptc_smp.exception.image;

public class ImageSendException extends RuntimeException {
    public ImageSendException() {
        super("При отправке изображения что-то пошло не так");
    }
}
