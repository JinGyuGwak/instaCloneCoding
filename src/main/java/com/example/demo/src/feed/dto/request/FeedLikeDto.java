package com.example.demo.src.feed.dto.request;


import com.example.demo.src.feed.entitiy.FeedLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedLikeDto {
    private Long feedId;
    private Long userId;

    public FeedLikeDto(FeedLike feedLike){
        this.feedId=feedLike.getFeed().getId();
        this.userId=feedLike.getUser().getId();
    }

}
