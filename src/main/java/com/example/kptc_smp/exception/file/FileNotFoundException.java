package com.example.kptc_smp.exception.file;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("Файл не найден");
    }
}
