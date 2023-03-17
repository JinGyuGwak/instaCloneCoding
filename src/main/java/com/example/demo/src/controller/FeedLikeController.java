package com.example.demo.src.controller;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.domain.feed.model.feedLike.FeedLikeDto;
import com.example.demo.src.domain.feed.model.feedLike.FeedLikeRes;
import com.example.demo.src.domain.feed.model.feedLike.GetFeedLike;
import com.example.demo.src.service.FeedLikeService;
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
    public BaseResponse<FeedLikeRes> userLikeFeed(@RequestBody FeedLikeDto feedLikeDto) {
        FeedLikeRes feedLikeRes = feedLikeService.userLikeFeed(feedLikeDto);
        return new BaseResponse<>(feedLikeRes);
    }

    /**
     * 좋아요 삭제
     * [DELETE] /feed/liked
     * 좋아요는 STATE가 아닌 DB에서 바로 삭제
     * 리퀘스트바디에 userId,feedId 넣기
     */
    @ResponseBody
    @DeleteMapping("/liked")
    public BaseResponse<String> feedLikeDelete(@RequestBody FeedLikeDto feedLikeDto) {
        feedLikeService.feedLikeDelete(feedLikeDto.getFeedId(), feedLikeDto.getUserId());
        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
    /**
     * 좋아요 조회
     * [GET] /feed/{feedId}/liked
     */
    @ResponseBody
    @GetMapping("/{feedId}/liked")
    public BaseResponse<List<GetFeedLike>> feedLikeSearch(@PathVariable Long feedId) {

        List<GetFeedLike> getFeedLikeList = feedLikeService.feedLikeSearch(feedId);
        return new BaseResponse<>(getFeedLikeList);
    }
}
