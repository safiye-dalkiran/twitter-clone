package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.RetweetRequestDto;

import java.security.Principal;

public interface RetweetService {
    void create(RetweetRequestDto dto, Principal principal);
    void delete(Long id, Principal principal);
}