package com.example.kptc_smp.exception.image;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException() {
        super("При загрузке изображения что-то пошло не так");
    }
}
