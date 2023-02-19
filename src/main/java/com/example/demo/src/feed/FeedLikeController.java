package com.example.demo.src.feed;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.feed.model.feedLike.FeedLikeRes;
import com.example.demo.src.feed.model.feedLike.GetFeedLike;
import com.example.demo.src.feed.model.feedLike.PostFeedLikeDto;
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
     * [POST] /feed/{feedId}/liked
     * 리퀘스트바디에 userId넣기
     */
    @ResponseBody
    @PostMapping("{feedId}/liked")
    public BaseResponse<FeedLikeRes> userLikeFeed(@PathVariable Long feedId,
                                                  @RequestBody Long userId) {
        FeedLikeRes feedLikeRes = feedLikeService.userLikeFeed(feedId, userId);
        return new BaseResponse<>(feedLikeRes);
    }

    /**
     * 좋아요 삭제
     * [DELETE] /feed/{feedId}/liked
     * 좋아요는 STATE가 아닌 DB에서 바로 삭제
     * 유저 아이디도 받야야해
     */
    @ResponseBody
    @DeleteMapping("{feedId}/liked")
    public BaseResponse<String> feedLikeDelete(@PathVariable Long feedId,
                                               @RequestBody PostFeedLikeDto postFeedLikeDto) {
        feedLikeService.feedLikeDelete(feedId, postFeedLikeDto.getUserId());
        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
    /**
     * 좋아요 조회
     * [GET] /feed/{feedId}/liked
     */
    @ResponseBody
    @GetMapping("{feedId}/liked")
    public BaseResponse<List<GetFeedLike>> feedLikeSearch(@PathVariable Long feedId) {

        List<GetFeedLike> getFeedLikeList = feedLikeService.feedLikeSearch(feedId);
        return new BaseResponse<>(getFeedLikeList);
    }
}
