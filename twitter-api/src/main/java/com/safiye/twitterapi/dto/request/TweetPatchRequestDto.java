package com.safiye.twitterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TweetPatchRequestDto(
        @NotBlank
        @Size(max = 280)
        String content

) {

}
