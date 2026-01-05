package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.RetweetRequestDto;
import com.safiye.twitterapi.entity.Retweet;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.RetweetNotFoundException;
import com.safiye.twitterapi.exceptions.TweetNotFoundException;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.mapper.RetweetMapper;
import com.safiye.twitterapi.repository.RetweetRepository;
import com.safiye.twitterapi.repository.TweetRepository;
import com.safiye.twitterapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final RetweetMapper retweetMapper;

    @Transactional
    @Override
    public void create(RetweetRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Tweet tweet = tweetRepository.findById(dto.tweetId())
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı"));

        if (retweetRepository.existsByUserIdAndTweetId(user.getId(), dto.tweetId())) {
            throw new RetweetNotFoundException("Bu tweeti zaten retweetlediniz!");
        }

        Retweet retweet = retweetMapper.toEntity(user, tweet);
        retweetRepository.save(retweet);
    }

    @Transactional
    @Override
    public void delete(Long id, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Retweet retweet = retweetRepository.findById(id)
                .orElseThrow(() -> new RetweetNotFoundException("Silinecek retweet bulunamadı"));

        if (!retweet.getUser().getId().equals(user.getId())) {
            throw new RetweetNotFoundException("Bu retweet'i silme yetkiniz yok!");
        }

        retweetRepository.delete(retweet);
    }

}