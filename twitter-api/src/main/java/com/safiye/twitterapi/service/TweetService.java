package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.TweetPatchRequestDto;
import com.safiye.twitterapi.dto.request.TweetRequestDto;
import com.safiye.twitterapi.dto.response.TweetResponseDto;


import java.security.Principal;
import java.util.List;

public interface TweetService {
    List<TweetResponseDto> findAll();

    // GET
    List<TweetResponseDto> findByUserId(Long userId);

    // GET
    TweetResponseDto findById(Long id);

    // POST
    TweetResponseDto create(TweetRequestDto tweetRequestDto, Principal principal);

    // PUT
    TweetResponseDto replaceOrCreate(Long id, TweetRequestDto tweetRequestDto, Principal principal);

    //PATCH
    TweetResponseDto update(Long id, TweetPatchRequestDto tweetPatchRequestDto, Principal principal);

    // DELETE
    void deleteById(Long id, Principal userId);
}