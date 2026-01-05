package com.safiye.twitterapi.dto.response;

import java.util.Set;

public record LoginResponseDto(
        Long id,
        String username,
        String message,
        Set<String> roles
) {}