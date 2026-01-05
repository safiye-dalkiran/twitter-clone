package com.safiye.twitterapi.dto.request;

public record RegisterRequestDto(
      String firstName,
      String lastName,
        String email,
        String password,
        String username) {
}
