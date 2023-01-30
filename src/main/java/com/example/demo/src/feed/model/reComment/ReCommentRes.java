package com.example.demo.src.feed.model.reComment;

import com.example.demo.src.feed.entity.ReComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReCommentRes {
    private Long id; // ReCommentId
    private Long commentId;
    private Long userId;
    private String userName;
    private String reComment;

    public ReCommentRes(ReComment reComment){
        this.id=reComment.getId();
        this.commentId=reComment.getFeedComment().getId();
        this.userId=reComment.getUser().getId();
        this.userName=reComment.getUser().getName();
        this.reComment=reComment.getReCommentText();
    }
}
