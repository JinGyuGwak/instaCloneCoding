package com.example.demo.src.response;

import com.example.demo.src.entity.ReComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetReCommentRes {
    private String username;
    private String commentText;
    private LocalDateTime updateDate;

    public GetReCommentRes(ReComment reComment){
        this.username=reComment.getUser().getName();
        this.commentText= reComment.getReCommentText();
        this.updateDate=reComment.getUpdatedAt();
    }
}
