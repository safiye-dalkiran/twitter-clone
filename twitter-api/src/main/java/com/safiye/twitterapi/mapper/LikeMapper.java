package com.safiye.twitterapi.mapper;

import com.safiye.twitterapi.entity.Like;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    public Like toEntity(User user, Tweet tweet) {
        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        return like;
    }
}