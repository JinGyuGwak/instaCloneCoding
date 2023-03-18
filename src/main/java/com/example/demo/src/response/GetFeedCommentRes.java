package com.example.demo.src.response;


import com.example.demo.src.entity.FeedComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedCommentRes {
    private String username;
    private String commentText;
    private LocalDateTime updateDate;

    public GetFeedCommentRes(FeedComment feedComment){
        this.username=feedComment.getUser().getName();
        this.commentText= feedComment.getCommentText();
        this.updateDate=feedComment.getUpdatedAt();
    }
}
