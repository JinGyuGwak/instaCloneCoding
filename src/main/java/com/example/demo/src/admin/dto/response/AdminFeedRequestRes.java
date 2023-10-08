package com.example.demo.src.admin.dto.response;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.feed.entitiy.Feed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
