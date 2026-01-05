package com.safiye.twitterapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record LikeRequestDto(
        @NotNull @JsonProperty("tweetId") Long tweetId
) {}