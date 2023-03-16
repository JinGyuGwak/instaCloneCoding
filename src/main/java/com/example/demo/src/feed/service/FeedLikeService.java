package com.example.demo.src.feed.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.entity.FeedLike;
import com.example.demo.src.feed.model.feedLike.FeedLikeDto;
import com.example.demo.src.feed.model.feedLike.FeedLikeRes;
import com.example.demo.src.feed.model.feedLike.GetFeedLike;
import com.example.demo.src.feed.repository.FeedLikeRepository;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_FEED;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;


    //피드 좋아요 (post)
    public FeedLikeRes userLikeFeed(FeedLikeDto feedLikeDto){
        Feed feed = funcFeed.findFeedByIdAndState(feedLikeDto.getFeedId());
        User user = funcUser.findUserByIdAndState(feedLikeDto.getUserId());
        FeedLike feedLike = new FeedLike(feed,user);

        //좋아요 누른 피드가 아니면 좋아요 생성
        if(!feedLikeRepository.findByFeedIdAndUserId(
                feedLikeDto.getFeedId(),feedLikeDto.getUserId()).isPresent()){
            feedLikeRepository.save(feedLike);
        }
        return new FeedLikeRes(feedLike);
    }

    //좋아요 삭제
    public void feedLikeDelete(Long feedId, Long userId){
        FeedLike feedLike=feedLikeRepository.findByFeedIdAndUserId(feedId,userId)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        feedLikeRepository.delete(feedLike);
    }

    //좋아요 조회
    public List<GetFeedLike> feedLikeSearch(Long feedId){
        return feedLikeRepository.findByFeedId(feedId)
                .stream()
                .map(GetFeedLike::new)
                .collect(Collectors.toList());
    }
}
