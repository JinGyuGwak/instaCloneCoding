package com.example.demo.src.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.entity.Feed;
import com.example.demo.src.entity.FeedLike;
import com.example.demo.src.request.FeedLikeDto;
import com.example.demo.src.response.GetFeedLikeRes;
import com.example.demo.src.repository.FeedLikeRepository;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
