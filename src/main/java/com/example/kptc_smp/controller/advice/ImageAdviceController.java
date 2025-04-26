package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.image.ImageException;
import com.example.kptc_smp.exception.image.ImageInvalidFormatException;
import com.example.kptc_smp.exception.image.ImageSendException;
import com.example.kptc_smp.exception.image.ImageUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ImageAdviceController {

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ResponseDto> imageException(ImageException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ImageInvalidFormatException.class)
    public ResponseEntity<ResponseDto> imageInvalidFormatException(ImageInvalidFormatException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ImageSendException.class)
    public ResponseEntity<ResponseDto> imageSendException(ImageSendException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ResponseDto> imageUploadException(ImageUploadException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
