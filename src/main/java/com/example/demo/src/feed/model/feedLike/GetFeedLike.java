package com.example.demo.src.feed.model.feedLike;


import com.example.demo.src.feed.entity.FeedLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetFeedLike {
    private Long userId;

    public GetFeedLike(FeedLike feedLike){
        this.userId= feedLike.getUser().getId();
    }
}
