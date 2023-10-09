package com.example.demo.src.feed.dto.response;


import com.example.demo.src.feed.entitiy.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedRes {
    private Long feedId; //postId
    private Long userId; //userId

    public PostFeedRes(Feed feed) {
        this.feedId = feed.getId();
        this.userId=feed.getUser().getId();
    }
}