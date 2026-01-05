package com.safiye.twitterapi.service;
import com.safiye.twitterapi.dto.request.CommentPatchRequestDto;
import com.safiye.twitterapi.dto.request.CommentRequestDto;
import com.safiye.twitterapi.dto.response.CommentResponseDto;
import com.safiye.twitterapi.entity.Comment;
import com.safiye.twitterapi.entity.Tweet;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.CommentNotFoundException;
import com.safiye.twitterapi.exceptions.TweetNotFoundException;
import com.safiye.twitterapi.exceptions.TwitterException;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.mapper.CommentMapper;
import com.safiye.twitterapi.repository.CommentRepository;
import com.safiye.twitterapi.repository.TweetRepository;
import com.safiye.twitterapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final TweetRepository tweetRepository;
    @Transactional
    @Override
    public CommentResponseDto create(CommentRequestDto dto, Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        Tweet tweet = tweetRepository.findById(dto.tweetId())
                .orElseThrow(() -> new TweetNotFoundException("Tweet bulunamadı"));

        Comment comment = commentMapper.toEntity(dto, user, tweet);
        return commentMapper.toResponseDto(commentRepository.save(comment));
    }


    @Override
    public List<CommentResponseDto> findByTweetId(Long tweetId) {
        return commentRepository.findByTweetId(tweetId)
                .stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }


    @Transactional
    @Override
    public void deleteById(Long id, Principal principal) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Yorum bulunamadı"));

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        boolean isCommentOwner = comment.getUser().getId().equals(currentUser.getId());
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(currentUser.getId());

        if (!isCommentOwner && !isTweetOwner) {
            throw new TwitterException(
                    "Bu yorumu silme yetkiniz yok!",
                    HttpStatus.FORBIDDEN
            );
        }

        commentRepository.delete(comment);
    }


    @Transactional
    @Override
    public CommentResponseDto update(Long id, CommentPatchRequestDto dto, Principal principal) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Güncellenecek yorum bulunamadı: " + id));

        User currentUser = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new TwitterException(
                    "Sadece kendi yorumunuzu düzenleyebilirsiniz!",
                    HttpStatus.FORBIDDEN
            );
        }

        commentMapper.updateEntity(comment, dto);
        return commentMapper.toResponseDto(commentRepository.save(comment));
    }


}
