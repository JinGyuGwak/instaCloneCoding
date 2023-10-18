package com.example.demo.src.feed.controller;

import com.example.demo.src.feed.dto.FeedLikeDto;
import com.example.demo.src.feed.dto.FeedLikeDto.GetFeedLikeRes;
import com.example.demo.src.feed.service.FeedLikeService;
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
public class FeedLikeController {
    private final FeedLikeService feedLikeService;

    /**
     * 피드 좋아요
     * [POST] /feed/liked
     * 리퀘스트바디에 userId,feedId넣기
     */
    @PostMapping("/liked")
    public ResponseEntity<FeedLikeDto> userLikeFeed(@RequestBody FeedLikeDto feedLikeDto) {
        return new ResponseEntity<>(feedLikeService.userLikeFeed(feedLikeDto), HttpStatus.OK);
    }

    /**
     * 좋아요 삭제
     * [DELETE] /feed/liked
     * 좋아요는 STATE가 아닌 DB에서 바로 삭제
     * 리퀘스트바디에 userId,feedId 넣기
     */
    @DeleteMapping("/liked")
    public ResponseEntity<String> feedLikeDelete(@RequestBody FeedLikeDto feedLikeDto) {
        feedLikeService.feedLikeDelete(feedLikeDto);
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    /**
     * 좋아요 조회
     * [GET] /feed/{feedId}/liked
     */
    @GetMapping("/{feedId}/liked")
    public ResponseEntity<List<GetFeedLikeRes>> feedLikeSearch(@PathVariable Long feedId) {
        return new ResponseEntity<>(feedLikeService.feedLikeSearch(feedId),HttpStatus.OK);
    }
}
