package com.example.demo.src.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> IllegalArgumentExceptionHandle(IllegalArgumentException exception, WebRequest webRequest) {
        log.warn("IllegalArgumentException. error message: {}", exception.getMessage());
        return new org.springframework.http.ResponseEntity<>(
                ExceptionRes.builder()
                        .status(400)
                        .timestamp(LocalDateTime.now())
                        .message(exception.getMessage())
                        .details(webRequest.getDescription(true))
                        .build()
                ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> ValidExceptionHandle(Exception exception, WebRequest webRequest) {
        log.error("ValidException has occured. ", exception);
        return new org.springframework.http.ResponseEntity<>(
                ExceptionRes.builder()
                        .status(400)
                        .timestamp(LocalDateTime.now())
                        .message(exception.getMessage())
                        .details(webRequest.getDescription(true))
                        .build()
                        ,HttpStatus.BAD_REQUEST);
    }


}
