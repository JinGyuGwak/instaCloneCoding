package com.example.demo.src.feed.model.feed;

import com.example.demo.src.feed.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponseDto {
    private Long id;
    private String postText;
    private String username;

    public FeedResponseDto(Feed feed){
        this.id=feed.getId();
        this.postText=feed.getPostText();
        this.username=feed.getUser().getName();
    }

}
