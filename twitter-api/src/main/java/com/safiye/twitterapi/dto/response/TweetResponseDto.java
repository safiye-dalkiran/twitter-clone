package com.safiye.twitterapi.dto.response;

import java.time.LocalDateTime;

public record TweetResponseDto(
        Long id,
        Long userId,
        String content,
        LocalDateTime createdAt,
        String username
) {}
