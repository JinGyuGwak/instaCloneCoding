package com.example.demo.src.comment.dto.response;

import com.example.demo.src.comment.entity.CommentLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentLikeRes {
    private Long commentId;
    private Long userId;

    public CommentLikeRes(CommentLike commentLike){
        this.commentId=commentLike.getFeedComment().getId();
        this.userId=commentLike.getUser().getId();
    }
}
