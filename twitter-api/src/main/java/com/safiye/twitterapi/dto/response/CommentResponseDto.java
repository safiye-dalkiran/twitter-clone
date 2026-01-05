package com.safiye.twitterapi.dto.response;

public record CommentResponseDto(
        Long id,         // React bunu silmek için kullanacak
        Long userId,
        String content,
        String username,
        java.time.LocalDateTime createdAt
) {}