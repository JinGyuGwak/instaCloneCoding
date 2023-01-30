package com.example.demo.src.feed.model.reComment;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UpdateReCommentRes {
    private Long reCommentId;
    private String reCommentText;


    public UpdateReCommentRes(Long reCommentId,String reCommentText) {
        this.reCommentId = reCommentId;
        this.reCommentText = reCommentText;
    }
}
