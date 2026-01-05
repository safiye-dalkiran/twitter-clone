package com.safiye.twitterapi.exceptions;

import org.springframework.http.HttpStatus;

public class TweetNotFoundException extends TwitterException {
    public TweetNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
