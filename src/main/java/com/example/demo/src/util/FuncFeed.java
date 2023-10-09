package com.example.demo.src.util;

import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class FuncFeed {
    private final FeedRepository feedRepository;

    public Feed findFeedByIdAndState(Long feedId){
        return feedRepository.findByIdAndState(feedId,ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }



}
