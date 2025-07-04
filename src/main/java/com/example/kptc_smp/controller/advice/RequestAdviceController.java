package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.stream.Collectors;

@ControllerAdvice
public class RequestAdviceController {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseDto("Метод не поддерживается: " + e.getMethod()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> httpMessageNotReadable() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseDto("Требуемый текст запроса отсутствует"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto> handleMaxSizeException() {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ResponseDto("Превышен максимальный размер файла (2MB)!"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDto> multipartException(MultipartException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "неизвестный тип";

        if (ex.getValue() == null) {
            String message = String.format("Не передан параметр (ожидался тип %s)", requiredType);
            return ResponseEntity.badRequest().body(new ResponseDto(message));
        }

        String actualType = ex.getValue().getClass().getSimpleName();
        String message = String.format(
                "Ошибка преобразования: '%s' в тип %s (недопустимое значение: '%s')",
                actualType,
                requiredType,
                ex.getValue()
        );

        return ResponseEntity.badRequest().body(new ResponseDto(message));
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ResponseDto> missingServletRequestPartException(MissingServletRequestPartException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto> constraintViolationException(ConstraintViolationException e) {
        String messages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(new ResponseDto(messages));
    }
}
