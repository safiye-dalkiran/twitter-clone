package com.safiye.twitterapi.controller;

import com.safiye.twitterapi.dto.request.LikeRequestDto;
import com.safiye.twitterapi.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3200", allowCredentials = "true")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public String like(
            @Validated @RequestBody LikeRequestDto likeRequestDto,
            Principal principal) {

        likeService.save(likeRequestDto, principal);
        return "Tweet beğenildi!";
    }

    @PostMapping("/dislike")
    public String dislike(
            @Validated @RequestBody LikeRequestDto dto,
            Principal principal) {

        likeService.delete(dto, principal);
        return "Beğeni geri çekildi!";
    }

}