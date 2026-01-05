package com.safiye.twitterapi.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotBlank @Size(max = 280)
        String content,
        @NotNull
        Long tweetId
) {}