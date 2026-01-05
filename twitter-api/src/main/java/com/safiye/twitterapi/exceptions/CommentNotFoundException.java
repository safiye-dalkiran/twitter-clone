package com.safiye.twitterapi.exceptions;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends TwitterException {
    public CommentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
