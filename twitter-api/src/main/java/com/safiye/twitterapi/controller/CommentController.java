package com.safiye.twitterapi.controller;

import com.safiye.twitterapi.dto.request.CommentPatchRequestDto;
import com.safiye.twitterapi.dto.request.CommentRequestDto;
import com.safiye.twitterapi.dto.response.CommentResponseDto;
import com.safiye.twitterapi.service.CommentService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3200", allowCredentials = "true")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/tweets/{tweetId}")
    public List<CommentResponseDto> findByTweetId(@Positive @PathVariable("tweetId") Long tweetId) {
        return commentService.findByTweetId(tweetId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto create(
            @Validated @RequestBody CommentRequestDto dto,
            Principal principal) {

        return commentService.create(dto, principal);
    }


    @PatchMapping("/{id}")
    public CommentResponseDto update(
            @Positive @PathVariable Long id,
            @Validated @RequestBody CommentPatchRequestDto commentPatchRequestDto,
            Principal principal) {

        return commentService.update(id, commentPatchRequestDto, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Positive @PathVariable Long id,
            Principal principal) {

        commentService.deleteById(id, principal);
    }

}