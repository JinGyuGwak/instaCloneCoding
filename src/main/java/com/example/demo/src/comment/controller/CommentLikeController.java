package com.example.demo.src.comment.controller;

import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.comment.dto.response.CommentLikeRes;
import com.example.demo.src.comment.dto.response.GetCommentLike;
import com.example.demo.src.feed.dto.request.FeedLikeDto;
import com.example.demo.src.comment.service.CommentLikeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseBody
    @PostMapping("/commentLiked")
    public ResponseEntityCustom<CommentLikeRes> commentLike(@RequestBody PostCommentLikeDto postCommentLikeDto) {
        CommentLikeRes commentLikeRes = commentLikeService
                .commentLike(postCommentLikeDto.getUserId(), postCommentLikeDto.getCommentId());
        return new ResponseEntityCustom<>(commentLikeRes);
    }

    /**
     * 댓글 좋아요 조회
     * [GET] /feed/comment/commentLiked
     */
    @ResponseBody
    @GetMapping("comment/commentLiked/{feedCommentId}")
    public ResponseEntityCustom<List<GetCommentLike>> commentLikeSearch(@PathVariable Long feedCommentId) {
        List<GetCommentLike> getCommentLikeList = commentLikeService.commentLikeSearch(feedCommentId);
        return new ResponseEntityCustom<>(getCommentLikeList);
    }

    /**
     * 댓글 좋아요 삭제
     * [DELETE] /feed/comment/{commentLikeId}
     * 좋아요는 State없이 DB에서 바로 삭제할꺼임
     */
    @ResponseBody
    @DeleteMapping("feed/comment/{commentId}")
    public ResponseEntityCustom<String> feedLikeDelete1(@PathVariable Long commentId,
                                                        @RequestBody FeedLikeDto feedLikeDto) {
        commentLikeService.commentLikeDelete(commentId, feedLikeDto.getUserId());
        String result = "삭제 완료!!";
        return new ResponseEntityCustom<>(result);
    }

    @Getter
    private static class PostCommentLikeDto{
        private Long userId;
        private Long commentId;
    }


}
