package com.example.demo.src.feed.controller;

import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.feed.dto.request.FeedLikeDto;
import com.example.demo.src.feed.dto.response.GetFeedLikeRes;
import com.example.demo.src.feed.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseBody
    @PostMapping("/liked")
    public ResponseEntityCustom<FeedLikeDto> userLikeFeed(@RequestBody FeedLikeDto feedLikeDto) {
        FeedLikeDto feedLikeRes = feedLikeService.userLikeFeed(feedLikeDto);
        return new ResponseEntityCustom<>(feedLikeRes);
    }

    /**
     * 좋아요 삭제
     * [DELETE] /feed/liked
     * 좋아요는 STATE가 아닌 DB에서 바로 삭제
     * 리퀘스트바디에 userId,feedId 넣기
     */
    @ResponseBody
    @DeleteMapping("/liked")
    public ResponseEntityCustom<String> feedLikeDelete(@RequestBody FeedLikeDto feedLikeDto) {
        feedLikeService.feedLikeDelete(feedLikeDto.getFeedId(), feedLikeDto.getUserId());
        String result = "삭제 완료!!";
        return new ResponseEntityCustom<>(result);
    }
    /**
     * 좋아요 조회
     * [GET] /feed/{feedId}/liked
     */
    @ResponseBody
    @GetMapping("/{feedId}/liked")
    public ResponseEntityCustom<List<GetFeedLikeRes>> feedLikeSearch(@PathVariable Long feedId) {

        List<GetFeedLikeRes> getFeedLikeResList = feedLikeService.feedLikeSearch(feedId);
        return new ResponseEntityCustom<>(getFeedLikeResList);
    }
}
