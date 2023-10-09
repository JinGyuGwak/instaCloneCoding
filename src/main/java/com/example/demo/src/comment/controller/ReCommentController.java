package com.example.demo.src.comment.controller;

import com.example.demo.src.comment.dto.ReCommentDto;
import com.example.demo.src.comment.dto.ReCommentDto.GetReCommentRes;
import com.example.demo.src.comment.dto.ReCommentDto.ReCommentRes;
import com.example.demo.src.comment.dto.ReCommentDto.UpdateReCommentRes;
import com.example.demo.src.comment.service.ReCommentService;
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
public class ReCommentController {
    private final ReCommentService reCommentService;
    /**
     * 리댓글 조회
     * [GET] /feed/comment/{feedId}
     * 댓글 아이디로 리댓글 조회
     */
    @GetMapping("/recomment/{commentId}")
    public ResponseEntity<List<GetReCommentRes>> searchReComment(@PathVariable Long commentId){
        return new ResponseEntity<>(reCommentService.searchReComment(commentId), HttpStatus.OK);
    }

    /**
     * 리댓글 달기
     * [POST] /feed/{feedId}/comment
     * @return BaseResponse<FeedCommentRes>
     */
    @PostMapping("/recomment/{commentId}")
    public ResponseEntity<ReCommentRes> createReComment(@PathVariable Long commentId,
                                                      @RequestBody ReCommentDto reCommentDto) {
        return new ResponseEntity<>(reCommentService.createReComment(commentId,reCommentDto.getReComment()),HttpStatus.OK);
    }

    /**
     * 리댓글 수정
     * [PATCH] /feed/comment/{commentid}
     */
    @PatchMapping("/recomment/{reCommentId}")
    public ResponseEntity<UpdateReCommentRes> updateReComment(@PathVariable Long reCommentId,
                                                                    @RequestBody ReCommentDto updateComment) {

        return new ResponseEntity<>(reCommentService.updateReComment(reCommentId,updateComment.getReComment()),HttpStatus.OK);
    }
    /**
     * 리댓글 삭제
     * [DELETE] /recomment/{recommentId}
     */
    @DeleteMapping("/recomment/{recommentId}")
    public ResponseEntity<String> deleteReComment(@PathVariable("recommentId") Long id){
        reCommentService.deleteReComment(id);

        String result = "삭제 완료!!";
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
