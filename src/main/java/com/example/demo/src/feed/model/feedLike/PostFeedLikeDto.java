package com.example.demo.src.feed.model.feedLike;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFeedLikeDto {

    private Long userId;
    public PostFeedLikeDto(Long userId){
        this.userId=userId;
    }
}
