package com.safiye.twitterapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record UserPatchRequestDto(
        @Size(max = 50)
        @JsonProperty("first_name")
        String firstName,

        @Size(max = 50)
        @JsonProperty("last_name")
        String lastName,

        @Size(max = 30)
        @JsonProperty("username")
        String username,

        @Size(max = 100)
        @Email
        String email,

        @Size(max = 255)
        String password
) {
}
