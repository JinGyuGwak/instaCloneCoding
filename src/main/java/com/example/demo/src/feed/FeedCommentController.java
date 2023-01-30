package com.example.demo.src.feed;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.feed.model.feedComment.*;
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
    public BaseResponse<List<GetFeedCommentRes>> searchFeedComment(@PathVariable Long feedId){
        List<GetFeedCommentRes> getFeedRes = feedCommentService.searchFeedComment(feedId);
        return new BaseResponse<>(getFeedRes);
    }

    /**
     * 피드 댓글 달기
     * [POST] /feed/{feedId}/comment
     * @return BaseResponse<FeedCommentRes>
     */
    @ResponseBody
    @PostMapping("{feedId}/comment")
    public BaseResponse<FeedCommentRes> createComment(@PathVariable Long feedId,
                                                      @RequestBody FeedCommentDto feedCommentDto) {
        FeedCommentRes feedCommentRes = feedCommentService.createFeedComment(feedId,feedCommentDto);
        return new BaseResponse<>(feedCommentRes);
    }

    /**
     * 피드 댓글 수정
     * [PATCH] /feed/comment/{commentid}
     */
    @ResponseBody
    @PatchMapping("comment/{commentId}")
    public BaseResponse<UpdateFeedCommentRes> updateComment(@PathVariable Long commentId,
                                                            @RequestBody FeedCommentUpdateRequestDto updateComment) {
        UpdateFeedCommentRes updateFeedCommentRes =
                feedCommentService.updateFeedComment(commentId,updateComment.getFeedComment());
        return new BaseResponse<>(updateFeedCommentRes);
    }
    /**
     * 피드 댓글 삭제
     * [DELETE] /feed/comment/{commentId}
     */
    @ResponseBody
    @DeleteMapping("/comment/{commentid}")
    public BaseResponse<String> deleteComment(@PathVariable("commentid") Long id){
        feedCommentService.deleteFeedComment(id);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
}
