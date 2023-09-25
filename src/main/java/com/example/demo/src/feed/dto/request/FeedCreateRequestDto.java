package com.example.demo.src.feed.dto.request;

import com.example.demo.src.user.entitiy.User;
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
