package com.safiye.twitterapi.mapper;

import com.safiye.twitterapi.dto.request.TweetPatchRequestDto;
import com.safiye.twitterapi.dto.request.TweetRequestDto;
import com.safiye.twitterapi.dto.response.TweetResponseDto;
import com.safiye.twitterapi.entity.Tweet;

import org.springframework.stereotype.Component;

@Component
public class TweetMapper {

    // POST & PUT (create)
    public Tweet toEntity(TweetRequestDto dto) {
        Tweet tweet = new Tweet();
        tweet.setContent(dto.content());
        return tweet;
    }

    public TweetResponseDto toResponseDto(Tweet tweet) {
        return new TweetResponseDto(
                tweet.getId(),
                tweet.getUser().getId(),
                tweet.getContent(),
                tweet.getCreatedAt(),
                tweet.getUser().getUsername()
        );
    }

    // PUT → full update
    public void updateEntity(Tweet tweetToUpdate, TweetRequestDto dto) {
        tweetToUpdate.setContent(dto.content());
    }

    // PATCH → partial update
    public void updateEntity(Tweet tweetToUpdate, TweetPatchRequestDto dto) {
        if (dto.content() != null) {
            tweetToUpdate.setContent(dto.content());
        }}}
