package com.safiye.twitterapi.repository;

import com.safiye.twitterapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByTweetId(Long tweetId);
}
