package com.safiye.twitterapi.repository;

import com.safiye.twitterapi.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet,Long> {
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
}
