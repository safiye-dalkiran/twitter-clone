package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.LoginRequestDto;
import com.safiye.twitterapi.dto.request.RegisterRequestDto;
import com.safiye.twitterapi.dto.response.LoginResponseDto;
import com.safiye.twitterapi.dto.response.RegisterResponseDto;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
