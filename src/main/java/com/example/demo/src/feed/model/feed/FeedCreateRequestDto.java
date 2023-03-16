package com.example.demo.src.feed.model.feed;

import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
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
