package com.example.demo.src.feed.model.feed;


import com.example.demo.src.feed.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedRes {
    private Long id; //postId
    private Long userId; //userId

    public PostFeedRes(Feed feed) {
        this.id = feed.getId();
        this.userId=feed.getUser().getId();
    }
}
