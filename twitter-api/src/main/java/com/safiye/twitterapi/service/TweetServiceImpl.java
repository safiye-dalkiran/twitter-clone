package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.TweetPatchRequestDto;
import com.safiye.twitterapi.dto.request.TweetRequestDto;
import com.safiye.twitterapi.dto.response.TweetResponseDto;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.TweetNotFoundException;
import com.safiye.twitterapi.exceptions.TwitterException;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.mapper.TweetMapper;
import com.safiye.twitterapi.repository.TweetRepository;
import com.safiye.twitterapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService{
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserRepository userRepository;


    @Override
    public List<TweetResponseDto> findAll() {
        return tweetRepository.findAll().stream()
                .map(tweetMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TweetResponseDto> findByUserId(Long userId) {
        return tweetRepository.findByUserId(userId)
                .stream()
                .map(tweetMapper::toResponseDto)
                .toList();
    }

    @Override
    public TweetResponseDto findById(Long id) {
        Optional<Tweet> optionalTweet=tweetRepository.findById(id);
        if(optionalTweet.isPresent()){
            Tweet tweet=optionalTweet.get();
            return tweetMapper.toResponseDto(tweet);
        }
        throw new TweetNotFoundException("Tweet bulunamadı, id :" +id);
    }

    //ya hep ya hiç
    @Transactional
    @Override
    public TweetResponseDto create(TweetRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Tweet tweet = tweetMapper.toEntity(dto);
        tweet.setUser(user);

        tweetRepository.save(tweet);
        return tweetMapper.toResponseDto(tweet);
    }

    @Transactional
    @Override
    public TweetResponseDto replaceOrCreate(Long id, TweetRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Tweet tweet;

        if (tweetRepository.existsById(id)) {
            tweet = tweetRepository.findById(id).orElseThrow();
            if (!tweet.getUser().getId().equals(user.getId())) {
                throw new TwitterException("Yetkisiz işlem", HttpStatus.FORBIDDEN);
            }
            tweetMapper.updateEntity(tweet,dto);
        } else {
            tweet = tweetMapper.toEntity(dto);
            tweet.setUser(user);
        }

        return tweetMapper.toResponseDto(tweetRepository.save(tweet));
    }


    @Transactional
    @Override
    public TweetResponseDto update(Long id, TweetPatchRequestDto dto, Principal principal) {

        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı"));

        if (!tweet.getUser().getUsername().equals(principal.getName())) {
            throw new TwitterException(
                    "Sadece kendi tweetinizi güncelleyebilirsiniz",
                    HttpStatus.FORBIDDEN
            );
        }

        tweetMapper.updateEntity(tweet, dto);
        return tweetMapper.toResponseDto(tweetRepository.save(tweet));
    }


    @Transactional
    @Override
    public void deleteById(Long id, Principal principal) {

        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetNotFoundException("Silinecek tweet bulunamadı"));

        if (!tweet.getUser().getUsername().equals(principal.getName())) {
            throw new TwitterException(
                    "Bu tweeti silmeye yetkiniz yok!",
                    HttpStatus.FORBIDDEN
            );
        }

        tweetRepository.delete(tweet);
    }

}
