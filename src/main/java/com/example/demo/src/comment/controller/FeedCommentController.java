package com.example.demo.src.comment.controller;

import com.example.demo.src.comment.dto.CommentDto.*;
import com.example.demo.src.comment.dto.CommentDto.FeedCommentDto;
import com.example.demo.src.comment.service.FeedCommentService;
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
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    /**
     * 피드 댓글 조회
     * [GET] /feed/comment/{feedId}
     */
    @GetMapping("/comment/{feedId}")
    public ResponseEntity<List<GetFeedCommentRes>> getFeedComment(@PathVariable Long feedId){
        return new ResponseEntity<>(feedCommentService.getFeedComment(feedId), HttpStatus.OK);
    }

    /**
     * 피드 댓글 달기
     * [POST] /feed/{feedId}/comment
     * @return BaseResponse<FeedCommentRes>
     */
    @PostMapping("/{feedId}/comment")
    public ResponseEntity<FeedCommentRes> createComment(@PathVariable Long feedId,
                                                        @RequestBody FeedCommentDto feedCommentDto) {
        return new ResponseEntity<>(feedCommentService.createFeedComment(feedCommentDto,feedId), HttpStatus.OK);
    }

    /**
     * 피드 댓글 수정
     * [PATCH] /feed/comment/{commentId}
     */
    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<UpdateFeedCommentRes> updateComment(@PathVariable Long commentId,
                                                              @RequestBody FeedCommentUpdateRequestDto updateComment) {
        return new ResponseEntity<>(feedCommentService.updateFeedComment(commentId,updateComment.getCommentText()), HttpStatus.OK);
    }
    /**
     * 피드 댓글 삭제
     * [DELETE] /feed/comment/{commentId}
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        feedCommentService.deleteFeedComment(commentId);
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
