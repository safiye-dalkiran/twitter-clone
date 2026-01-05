package com.safiye.twitterapi.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends TwitterException {

    //409
    public UserAlreadyRegisteredException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
