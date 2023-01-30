package com.example.demo.src.feed.model.feedLike;


import com.example.demo.src.feed.entity.FeedLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedLikeRes {
    private Long feedId;
    private Long userId;

    public FeedLikeRes(FeedLike feedLike){
        this.feedId=feedLike.getFeed().getId();
        this.userId=feedLike.getUser().getId();
    }

}
