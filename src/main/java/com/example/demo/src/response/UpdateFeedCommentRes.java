package com.example.demo.src.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFeedCommentRes {
    private Long commentId;
    private String feedComment;


    public UpdateFeedCommentRes(Long commentId,String feedComment) {
        this.commentId = commentId;
        this.feedComment = feedComment;
    }
}
