package com.safiye.twitterapi.mapper;

import com.safiye.twitterapi.dto.request.CommentPatchRequestDto;
import com.safiye.twitterapi.dto.request.CommentRequestDto;
import com.safiye.twitterapi.dto.response.CommentResponseDto;
import com.safiye.twitterapi.entity.Comment;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toEntity(CommentRequestDto commentRequestDto, User user, Tweet tweet) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.content());
        comment.setUser(user);
        comment.setTweet(tweet);
        return comment;
    }

    public CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getContent(),
                comment.getUser() != null ? comment.getUser().getUsername() : "Bilinmeyen Kullanıcı",
                comment.getCreatedAt() != null ? comment.getCreatedAt() : java.time.LocalDateTime.now()
        );
    }
    public void updateEntity(Comment commentToUpdate, CommentPatchRequestDto commentPatchRequestDto){
        if(commentPatchRequestDto.content()!=null){
            commentToUpdate.setContent(commentPatchRequestDto.content());
        }
    }
}