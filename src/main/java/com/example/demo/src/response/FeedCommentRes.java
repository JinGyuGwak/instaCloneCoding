package com.example.demo.src.response;


import com.example.demo.src.entity.FeedComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedCommentRes {
    private Long id; // FeedCommentId
    private Long feedId;
    private Long userId;
    private String username;
    private String commentText;

    public FeedCommentRes(FeedComment feedComment){
        this.id= feedComment.getId();
        this.feedId=feedComment.getFeed().getId();
        this.userId= feedComment.getUser().getId();
        this.username=feedComment.getUser().getName();
        this.commentText= feedComment.getCommentText();
    }
}
