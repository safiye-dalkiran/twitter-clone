package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.LikeRequestDto;

import java.security.Principal;

public interface LikeService {
    void save(LikeRequestDto likeRequestDto, Principal principal);
    void delete(LikeRequestDto likeRequestDto, Principal principal);
}