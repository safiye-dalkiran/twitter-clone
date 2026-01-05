package com.safiye.twitterapi.controller;

import com.safiye.twitterapi.dto.request.TweetPatchRequestDto;
import com.safiye.twitterapi.dto.request.TweetRequestDto;
import com.safiye.twitterapi.dto.response.TweetResponseDto;
import com.safiye.twitterapi.service.TweetService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;
    // TweetController.java

    @GetMapping("/all")
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.findAll(); // Tüm tweetleri çek
    }

    @GetMapping("/{id}")
    public TweetResponseDto getById(
            @Positive @PathVariable("id") Long id) {

        return tweetService.findById(id);
    }
    @GetMapping("/user/{userId}")
    public List<TweetResponseDto> getByUserId(@Positive @PathVariable("userId") Long userId) {
        return tweetService.findByUserId(userId);
    }



    // Create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto create(
            @Validated @RequestBody TweetRequestDto tweetRequestDto, Principal principal) {

        return tweetService.create(tweetRequestDto,principal);
    }

    // PUT (replace or create)
    @PutMapping("/{id}")
    public TweetResponseDto replaceOrCreate(
            @Positive @PathVariable Long id,
            @Validated @RequestBody TweetRequestDto tweetRequestDto,
            Principal principal) {

        return tweetService.replaceOrCreate(id, tweetRequestDto, principal);
    }

    @PatchMapping("/{id}")
    public TweetResponseDto update(
            @Positive @PathVariable Long id,
            @Validated @RequestBody TweetPatchRequestDto tweetPatchRequestDto,
            Principal principal) {

        return tweetService.update(id, tweetPatchRequestDto, principal);
    }


    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Positive @PathVariable Long id,
            Principal principal) {

        tweetService.deleteById(id, principal);
    }


}

