package com.example.demo.src.func;

import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.src.common.response.BaseResponseStatus.NOT_FIND_FEED;

@RequiredArgsConstructor
@Service
public class FuncFeed {
    private final FeedRepository feedRepository;

    public Feed findFeedByIdAndState(Long feedId){
        return feedRepository.findByIdAndState(feedId,ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_FEED));
    }



}
