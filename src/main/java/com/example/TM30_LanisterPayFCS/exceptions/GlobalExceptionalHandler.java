package com.example.TM30_LanisterPayFCS.exceptions;

import com.example.TM30_LanisterPayFCS.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> exceptionHandler(Exception exception){
        var response = ApiResponse.builder().error(exception.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }
}
