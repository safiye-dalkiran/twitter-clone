package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.LikeRequestDto;
import com.safiye.twitterapi.entity.Like;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.LikeNotFoundException;
import com.safiye.twitterapi.exceptions.TweetNotFoundException;
import com.safiye.twitterapi.exceptions.TwitterException;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.mapper.LikeMapper;
import com.safiye.twitterapi.repository.LikeRepository;
import com.safiye.twitterapi.repository.TweetRepository;
import com.safiye.twitterapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final LikeMapper likeMapper;

    @Transactional
    @Override
    public void save(LikeRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        boolean exists = likeRepository.existsByUserIdAndTweetId(user.getId(), dto.tweetId());
        if (exists) {
            throw new TwitterException("Bu tweeti zaten beğendiniz!", HttpStatus.BAD_REQUEST);
        }

        Tweet tweet = tweetRepository.findById(dto.tweetId())
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı"));

        Like like = likeMapper.toEntity(user, tweet);
        likeRepository.save(like);
    }
    @Transactional
    @Override
    public void delete(LikeRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Like like = likeRepository.findByUserIdAndTweetId(user.getId(), dto.tweetId())
                .orElseThrow(() -> new LikeNotFoundException(
                        "Beğeni bulunamadı, önce beğenmelisiniz!"
                ));

        likeRepository.delete(like);
    }

}