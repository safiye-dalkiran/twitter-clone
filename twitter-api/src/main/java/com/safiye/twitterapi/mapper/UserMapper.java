package com.safiye.twitterapi.mapper;

import com.safiye.twitterapi.dto.request.UserPatchRequestDto;
import com.safiye.twitterapi.dto.request.UserRequestDto;
import com.safiye.twitterapi.dto.response.UserResponseDto;
import com.safiye.twitterapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequestDto userRequestDto){
        User user = new User();
        user.setFirstName(userRequestDto.firstName());
        user.setLastName(userRequestDto.lastName());
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password());
        return user;

    }
    // Entity'yi Response DTO'ya çevirir (Dışarıya veri verirken - Şifresiz)
    public UserResponseDto toResponseDto(User user){

        return new UserResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername()

        );
    }
    public void updateEntity(User userToUpdate, UserPatchRequestDto userPatchRequestDto){

        if (userPatchRequestDto.firstName() != null)
            userToUpdate.setFirstName(userPatchRequestDto.firstName());
        if (userPatchRequestDto.lastName() != null)
            userToUpdate.setLastName(userPatchRequestDto.lastName());
        if (userPatchRequestDto.username() != null)
            userToUpdate.setUsername(userPatchRequestDto.username());
        if (userPatchRequestDto.email() != null)
            userToUpdate.setEmail(userPatchRequestDto.email());
        if (userPatchRequestDto.password() != null)
            userToUpdate.setPassword(userPatchRequestDto.password());
    }
}
