package com.safiye.twitterapi.mapper;

import com.safiye.twitterapi.entity.Retweet;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RetweetMapper {
    public Retweet toEntity(User user, Tweet tweet) {
        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet); // Orijinal tweet
        return retweet;
    }
}