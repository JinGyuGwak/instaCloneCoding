package com.example.demo.src.controller;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.domain.feed.model.commentLike.CommentLikeRes;
import com.example.demo.src.domain.feed.model.commentLike.GetCommentLike;
import com.example.demo.src.domain.feed.model.feedLike.FeedLikeDto;
import com.example.demo.src.service.CommentLikeService;
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
    public BaseResponse<CommentLikeRes> commentLike(@RequestBody PostCommentLikeDto postCommentLikeDto) {
        CommentLikeRes commentLikeRes = commentLikeService
                .commentLike(postCommentLikeDto.getUserId(), postCommentLikeDto.getCommentId());
        return new BaseResponse<>(commentLikeRes);
    }

    /**
     * 댓글 좋아요 조회
     * [GET] /feed/comment/commentLiked
     */
    @ResponseBody
    @GetMapping("comment/commentLiked/{feedCommentId}")
    public BaseResponse<List<GetCommentLike>> commentLikeSearch(@PathVariable Long feedCommentId) {
        List<GetCommentLike> getCommentLikeList = commentLikeService.commentLikeSearch(feedCommentId);
        return new BaseResponse<>(getCommentLikeList);
    }

    /**
     * 댓글 좋아요 삭제
     * [DELETE] /feed/comment/{commentLikeId}
     * 좋아요는 State없이 DB에서 바로 삭제할꺼임
     */
    @ResponseBody
    @DeleteMapping("feed/comment/{commentId}")
    public BaseResponse<String> feedLikeDelete1(@PathVariable Long commentId,
                                                @RequestBody FeedLikeDto feedLikeDto) {
        commentLikeService.commentLikeDelete(commentId, feedLikeDto.getUserId());
        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    @Getter
    private static class PostCommentLikeDto{
        private Long userId;
        private Long commentId;
    }


}
