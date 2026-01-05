package com.safiye.twitterapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public record UserRequestDto(
        @Size(max = 50)
        @NotNull
        @NotBlank
        @NotEmpty
        @JsonProperty("first_name")
        String firstName,

        @Size(max = 50)
        @NotNull
        @NotBlank
        @NotEmpty
        @JsonProperty("last_name")
        String lastName,

        @Size(max = 30)
        @NotBlank
        @JsonProperty("username")
        String username,

        @Size(max = 100)
        @Email
        String email,

        @Size(max = 255)
        String password
) {

}
