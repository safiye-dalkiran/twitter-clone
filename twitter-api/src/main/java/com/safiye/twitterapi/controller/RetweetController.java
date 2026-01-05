package com.safiye.twitterapi.controller;

import com.safiye.twitterapi.dto.request.RetweetRequestDto;
import com.safiye.twitterapi.service.RetweetService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retweet")
@CrossOrigin(origins = "http://localhost:3200", allowCredentials = "true")
public class RetweetController {

    private final RetweetService retweetService;

    // POST
    @PostMapping
    public String retweet(
            @Validated @RequestBody RetweetRequestDto retweetRequestDto,
            Principal principal) {

        retweetService.create(retweetRequestDto, principal);
        return "Tweet başarıyla retweetlendi!";
    }

    @DeleteMapping("/{id}")
    public String deleteRetweet(
            @Positive @PathVariable Long id,
            Principal principal) {

        retweetService.delete(id, principal);
        return "Retweet başarıyla geri alındı (silindi)!";
    }

}