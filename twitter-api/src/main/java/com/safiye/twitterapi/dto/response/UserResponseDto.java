package com.safiye.twitterapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("username")
        String username
) {
}
