package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.email.CodeExpireException;
import com.example.kptc_smp.exception.email.EmailFoundException;
import com.example.kptc_smp.exception.email.EmailNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmailAdviceController {

    @ExceptionHandler(EmailFoundException.class)
    public ResponseEntity<ResponseDto> emailException(EmailFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ResponseDto> emailNotFoundException(EmailNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(CodeExpireException.class)
    public ResponseEntity<ResponseDto> codeExpireException(CodeExpireException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }
}
