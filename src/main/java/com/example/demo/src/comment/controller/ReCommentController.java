package com.example.demo.src.comment.controller;

import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.comment.dto.request.FeedCommentUpdateRequestDto;
import com.example.demo.src.comment.dto.response.GetReCommentRes;
import com.example.demo.src.comment.dto.response.ReCommentRes;
import com.example.demo.src.comment.dto.response.UpdateReCommentRes;
import com.example.demo.src.comment.service.ReCommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class ReCommentController {
    private final ReCommentService reCommentService;
    /**
     * 리댓글 조회
     * [GET] /feed/comment/{feedId}
     * 댓글 아이디로 리댓글 조회
     */
    @ResponseBody
    @GetMapping("/recomment/{commentId}")
    public ResponseEntityCustom<List<GetReCommentRes>> searchReComment(@PathVariable Long commentId){
        List<GetReCommentRes> getReCommentRes = reCommentService.searchReComment(commentId);
        return new ResponseEntityCustom<>(getReCommentRes);
    }

    /**
     * 리댓글 달기
     * [POST] /feed/{feedId}/comment
     * @return BaseResponse<FeedCommentRes>
     */
    @ResponseBody
    @PostMapping("/recomment/{commentId}")
    public ResponseEntityCustom<ReCommentRes> createComment(@PathVariable Long commentId,
                                                            @RequestBody ReCommentDto reCommentDto) {
        ReCommentRes reCommentRes = reCommentService.createReComment(
                commentId,
                reCommentDto.getUserId(),
                reCommentDto.getReComment());
        return new ResponseEntityCustom<>(reCommentRes);
    }

    /**
     * 리댓글 수정
     * [PATCH] /feed/comment/{commentid}
     */
    @ResponseBody
    @PatchMapping("/recomment/{reCommentId}")
    public ResponseEntityCustom<UpdateReCommentRes> updateReComment(@PathVariable Long reCommentId,
                                                                    @RequestBody FeedCommentUpdateRequestDto updateComment) {
        UpdateReCommentRes updateReCommentRes =
                reCommentService.updateReComment(reCommentId,updateComment.getFeedComment());
        return new ResponseEntityCustom<>(updateReCommentRes);
    }
    /**
     * 리댓글 삭제
     * [DELETE] /recomment/{recommentId}
     */
    @ResponseBody
    @DeleteMapping("/recomment/{recommentId}")
    public ResponseEntityCustom<String> deleteReComment(@PathVariable("recommentId") Long id){
        reCommentService.deleteReComment(id);

        String result = "삭제 완료!!";
        return new ResponseEntityCustom<>(result);
    }

    @Getter
    private static class ReCommentDto{
        private Long userId;
        private String reComment;
    }
}
