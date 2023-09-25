package com.example.demo.src.feed.dto.response;


import com.example.demo.src.feed.entitiy.FeedLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetFeedLikeRes {
    private Long userId;

    public GetFeedLikeRes(FeedLike feedLike){
        this.userId= feedLike.getUser().getId();
    }
}
