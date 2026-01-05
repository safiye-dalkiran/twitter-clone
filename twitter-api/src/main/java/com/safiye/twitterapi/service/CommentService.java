package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.CommentPatchRequestDto;
import com.safiye.twitterapi.dto.request.CommentRequestDto;
import com.safiye.twitterapi.dto.response.CommentResponseDto;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    List<CommentResponseDto> findByTweetId(Long tweetId);
    CommentResponseDto create(CommentRequestDto commentRequestDto, Principal principal);
    CommentResponseDto update(Long id, CommentPatchRequestDto commentPatchRequestDto, Principal userId);
    void deleteById(Long id, Principal userId);
}