package com.example.demo.src.domain.feed.model.feed;

import com.example.demo.src.entity.User;
import lombok.*;

@Getter
@NoArgsConstructor
public class FeedCreateRequestDto {

    private User user;
    private String postText;

    public FeedCreateRequestDto(User user, String postText) {
        this.user=user;
        this.postText = postText;
    }
}
