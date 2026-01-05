package com.safiye.twitterapi.service;


import com.safiye.twitterapi.dto.request.UserPatchRequestDto;
import com.safiye.twitterapi.dto.request.UserRequestDto;
import com.safiye.twitterapi.dto.response.UserResponseDto;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.mapper.UserMapper;
import com.safiye.twitterapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {
        Optional<User> optionalUser=userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            return userMapper.toResponseDto(user);
        }
        throw new UserNotFoundException("User bulunamadı, id :" +id);
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User user =userMapper.toEntity(userRequestDto);
        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }


    @Override
    public UserResponseDto update(Long id, UserPatchRequestDto userPatchRequestDto, Principal principal) {
        User userToUpdate=userRepository
                .findById(id)
                .orElseThrow(()->new UserNotFoundException("User bulunamadı, id :" +id));
        userMapper.updateEntity(userToUpdate,userPatchRequestDto);
        userRepository.save(userToUpdate);
        return userMapper.toResponseDto(userToUpdate);
    }

    @Override
    public void deleteById(Long id, Principal principal) {
        userRepository.deleteById(id);
    }
}
