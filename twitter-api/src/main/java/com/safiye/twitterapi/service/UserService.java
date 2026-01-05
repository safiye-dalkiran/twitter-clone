package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.UserPatchRequestDto;
import com.safiye.twitterapi.dto.request.UserRequestDto;
import com.safiye.twitterapi.dto.response.UserResponseDto;

import java.security.Principal;
import java.util.List;

public interface UserService {
    //GET
    List<UserResponseDto> getAll();

    //GET
    UserResponseDto findById(Long id);

    //POST
    UserResponseDto create(UserRequestDto userRequestDto);


    //PATCH
    UserResponseDto update(Long id, UserPatchRequestDto userPatchRequestDto, Principal principal);

    //DELETE
    void deleteById(Long id, Principal principal);

}
