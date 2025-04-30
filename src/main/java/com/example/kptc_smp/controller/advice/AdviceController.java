package com.example.kptc_smp.controller.advice;

import com.example.kptc_smp.dto.ResponseDto;
import com.example.kptc_smp.exception.actionticket.ActionTicketExpireException;
import com.example.kptc_smp.exception.actionticket.ActionTicketNotFoundException;
import com.example.kptc_smp.exception.file.FileNotFoundException;
import com.example.kptc_smp.exception.google.GoogleDriveException;
import com.example.kptc_smp.exception.guild.OrderNotFoundException;
import com.example.kptc_smp.exception.news.NewsNotFoundException;
import com.example.kptc_smp.exception.zip.ZipException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ResponseDto> orderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ResponseDto> newsNotFoundException(NewsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ResponseDto> fileNotFoundException(FileNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ActionTicketNotFoundException.class)
    public ResponseEntity<ResponseDto> actionTicketNotFoundException(ActionTicketNotFoundException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ActionTicketExpireException.class)
    public ResponseEntity<ResponseDto> actionTicketExpireException(ActionTicketExpireException e) {
        return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(GoogleDriveException.class)
    public ResponseEntity<ResponseDto> googleDriveException(GoogleDriveException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(e.getMessage()));
    }

    @ExceptionHandler(ZipException.class)
    public ResponseEntity<ResponseDto> zipException(ZipException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto(e.getMessage()));
    }
}