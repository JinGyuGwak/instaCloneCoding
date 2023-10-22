package com.example.demo.src.comment.controller;

import com.example.demo.src.comment.dto.CommentLikeDto;
import com.example.demo.src.comment.service.CommentLikeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;
    /**
     * 댓글 좋아요
     * [POST] /feed/commentLiked
     */
    @PostMapping("/commentLiked/{commentId}")
    public ResponseEntity<CommentLikeDto> commentLike(@PathVariable Long commentId) {
        return new ResponseEntity<>(commentLikeService.commentLike(commentId), HttpStatus.OK);
    }

    /**
     * 댓글 좋아요 조회
     * [GET] /feed/comment/commentLiked
     */
    @GetMapping("/comment/commentLiked/{commentId}")
    public ResponseEntity<List<CommentLikeDto>> commentLikeSearch(@PathVariable Long commentId) {
        return new ResponseEntity<>(commentLikeService.commentLikeSearch(commentId), HttpStatus.OK);
    }

    /**
     * 댓글 좋아요 삭제
     * [DELETE] /feed/comment/{commentLikeId}
     * 좋아요는 State없이 DB에서 바로 삭제할꺼임
     */
    @DeleteMapping("/commentlike/{commentId}")
    public ResponseEntity<String> feedLikeDelete(@PathVariable Long commentId) {
        commentLikeService.commentLikeDelete(commentId);
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
