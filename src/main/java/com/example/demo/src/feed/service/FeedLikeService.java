package com.example.demo.src.feed.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.entity.FeedLike;
import com.example.demo.src.feed.model.feedLike.FeedLikeRes;
import com.example.demo.src.feed.model.feedLike.GetFeedLike;
import com.example.demo.src.feed.repository.FeedLikeRepository;
import com.example.demo.src.feed.repository.FeedRepository;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_FEED;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;
    //피드 좋아요 (post)
    public FeedLikeRes userLikeFeed(Long feedId, Long userId){
        Feed feed = funcFeed.selectFeedByIdAndState(feedId);
        User user = funcUser.selectUserByIdAndState(userId);
        FeedLike feedLike = new FeedLike(feed,user);

        if(!feedLikeRepository.findByFeedIdAndUserId(feedId, userId).isPresent()){
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
        List<FeedLike> feedLikeList = feedLikeRepository.findByFeedId(feedId);
        List<GetFeedLike> getFeedLikeList = new ArrayList<>();
        for(FeedLike feedLike : feedLikeList){
            GetFeedLike a = new GetFeedLike(feedLike);
            getFeedLikeList.add(a);
        }
        return getFeedLikeList;
    }
}
