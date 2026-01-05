package com.safiye.twitterapi.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TwitterException.class)
    public ResponseEntity<TwitterErrorResponse> handleException(TwitterException twitterException){
        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse();
        twitterErrorResponse.setStatus(twitterException.getHttpStatus().value());
        twitterErrorResponse.setMessage(twitterException.getMessage());
        twitterErrorResponse.setTimestamp(System.currentTimeMillis());
        twitterErrorResponse.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(twitterErrorResponse, twitterException.getHttpStatus());
    }
    //400
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<TwitterErrorResponse> handleException(MethodArgumentTypeMismatchException exception) {
            TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse();
        twitterErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        twitterErrorResponse.setMessage(exception.getMessage());
        twitterErrorResponse.setTimestamp(System.currentTimeMillis());
        twitterErrorResponse.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(twitterErrorResponse, HttpStatus.BAD_REQUEST);

    }
    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TwitterErrorResponse> handleException(MethodArgumentNotValidException exception) {
        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse();
        twitterErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        twitterErrorResponse.setMessage(exception.getMessage());
        twitterErrorResponse.setTimestamp(System.currentTimeMillis());
        twitterErrorResponse.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(twitterErrorResponse, HttpStatus.BAD_REQUEST);

    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<TwitterErrorResponse> handleException(Exception exception){

        log.error(exception.getMessage());
        log.error(exception.toString());

        TwitterErrorResponse twitterErrorResponse = new TwitterErrorResponse();
        twitterErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        twitterErrorResponse.setMessage("Beklenmedik bir hata oluştu: " + exception.getMessage());
        twitterErrorResponse.setTimestamp(System.currentTimeMillis());
        twitterErrorResponse.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(twitterErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}