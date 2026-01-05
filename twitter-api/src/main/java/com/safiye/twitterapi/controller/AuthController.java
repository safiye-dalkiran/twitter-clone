package com.safiye.twitterapi.controller;


import com.safiye.twitterapi.dto.request.LoginRequestDto;
import com.safiye.twitterapi.dto.request.RegisterRequestDto;
import com.safiye.twitterapi.dto.response.LoginResponseDto;
import com.safiye.twitterapi.dto.response.RegisterResponseDto;
import com.safiye.twitterapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
   public RegisterResponseDto register(@Validated @RequestBody RegisterRequestDto registerRequestDto){

        return authService.register(registerRequestDto);
    }

    //login
   @PostMapping("/login")
    public LoginResponseDto login(@Validated @RequestBody LoginRequestDto loginRequestDto){
       return authService.login(loginRequestDto);
    }

}
