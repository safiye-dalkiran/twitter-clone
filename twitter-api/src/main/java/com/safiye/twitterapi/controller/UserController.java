package com.safiye.twitterapi.controller;

import com.safiye.twitterapi.dto.request.UserPatchRequestDto;
import com.safiye.twitterapi.dto.request.UserRequestDto;
import com.safiye.twitterapi.dto.response.UserResponseDto;
import com.safiye.twitterapi.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@Positive @PathVariable("id") Long id){
        return userService.findById(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Validated @RequestBody UserRequestDto userRequestDto){
        return userService.create(userRequestDto);
    }
    @PatchMapping("/{id}")
    public UserResponseDto update(
            @Positive @PathVariable Long id,
            @Validated @RequestBody UserPatchRequestDto userPatchRequestDto,
            Principal principal) {

        return userService.update(id, userPatchRequestDto, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Positive @PathVariable Long id,
            Principal principal) {

        userService.deleteById(id, principal);
    }

}
