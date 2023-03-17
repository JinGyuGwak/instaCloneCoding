package com.example.demo.src.domain.admin.model;

import com.example.demo.src.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.example.demo.common.entity.BaseEntity.*;

@Getter
@NoArgsConstructor
public class AdminFeedRequestRes {
    private Long id;
    private Long userId;
    private String postText;
    private LocalDateTime createdAt;
    private State state;

    public AdminFeedRequestRes(Feed feed){
        this.id= feed.getId();
        this.userId= feed.getUser().getId();
        this.postText= feed.getPostText();
        this.createdAt=feed.getCreatedAt();
        this.state=feed.getState();
    }

}
