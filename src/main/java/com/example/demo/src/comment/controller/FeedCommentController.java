package com.example.demo.src.comment.controller;

import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.comment.dto.request.FeedCommentUpdateRequestDto;
import com.example.demo.src.comment.dto.response.FeedCommentRes;
import com.example.demo.src.comment.dto.response.GetFeedCommentRes;
import com.example.demo.src.comment.dto.response.UpdateFeedCommentRes;
import com.example.demo.src.comment.service.FeedCommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseBody
    @GetMapping("/comment/{feedId}")
    public ResponseEntityCustom<List<GetFeedCommentRes>> searchFeedComment(@PathVariable Long feedId){
        List<GetFeedCommentRes> getFeedRes = feedCommentService.searchFeedComment(feedId);
        return new ResponseEntityCustom<>(getFeedRes);
    }

    /**
     * 피드 댓글 달기
     * [POST] /feed/{feedId}/comment
     * @return BaseResponse<FeedCommentRes>
     */
    @ResponseBody
    @PostMapping("{feedId}/comment")
    public ResponseEntityCustom<FeedCommentRes> createComment(@PathVariable Long feedId,
                                                              @RequestBody FeedCommentDto feedCommentDto) {
        FeedCommentRes feedCommentRes = feedCommentService.createFeedComment(
                feedId,feedCommentDto.getUserId(), feedCommentDto.getFeedComment());
        return new ResponseEntityCustom<>(feedCommentRes);
    }

    /**
     * 피드 댓글 수정
     * [PATCH] /feed/comment/{commentid}
     */
    @ResponseBody
    @PatchMapping("comment/{commentId}")
    public ResponseEntityCustom<UpdateFeedCommentRes> updateComment(@PathVariable Long commentId,
                                                                    @RequestBody FeedCommentUpdateRequestDto updateComment) {
        UpdateFeedCommentRes updateFeedCommentRes =
                feedCommentService.updateFeedComment(commentId,updateComment.getFeedComment());
        return new ResponseEntityCustom<>(updateFeedCommentRes);
    }
    /**
     * 피드 댓글 삭제
     * [DELETE] /feed/comment/{commentId}
     */
    @ResponseBody
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntityCustom<String> deleteComment(@PathVariable Long commentId){
        feedCommentService.deleteFeedComment(commentId);

        String result = "삭제 완료!!";
        return new ResponseEntityCustom<>(result);
    }

    @Getter
    private static class FeedCommentDto{
        private Long userId;
        private String feedComment;
    }


}
