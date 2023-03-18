package com.example.demo.src.response;


import com.example.demo.src.entity.FeedLike;
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
