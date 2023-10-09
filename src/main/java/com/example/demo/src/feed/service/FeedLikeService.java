package com.example.demo.src.feed.service;

import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedLike;
import com.example.demo.src.feed.dto.request.FeedLikeDto;
import com.example.demo.src.feed.dto.response.GetFeedLikeRes;
import com.example.demo.src.feed.repository.FeedLikeRepository;
import com.example.demo.src.util.FuncFeed;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.response.BaseResponseStatus.NOT_FIND_FEED;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;


    //피드 좋아요 (post)
    @Transactional
    public FeedLikeDto userLikeFeed(FeedLikeDto feedLikeDto){
        Feed feed = funcFeed.findFeedByIdAndState(feedLikeDto.getFeedId());
        User user = funcUser.findUserByIdAndState(feedLikeDto.getUserId());
        FeedLike feedLike = new FeedLike(feed,user);

        //좋아요 누른 피드가 아니면 좋아요 생성
        if(!feedLikeRepository.findByFeedIdAndUserId(
                feedLikeDto.getFeedId(),feedLikeDto.getUserId()).isPresent()){
            feedLikeRepository.save(feedLike);
        }
        return new FeedLikeDto(feedLike);
    }

    //좋아요 삭제
    @Transactional
    public void feedLikeDelete(Long feedId, Long userId){
        FeedLike feedLike=feedLikeRepository.findByFeedIdAndUserId(feedId,userId)
                .orElseThrow(()-> new BaseException(NOT_FIND_FEED));
        feedLikeRepository.delete(feedLike);
    }

    //좋아요 조회
    @Transactional
    public List<GetFeedLikeRes> feedLikeSearch(Long feedId){
        return feedLikeRepository.findByFeedId(feedId)
                .stream()
                .map(GetFeedLikeRes::new)
                .collect(Collectors.toList());
    }
}
