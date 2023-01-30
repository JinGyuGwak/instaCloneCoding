package com.example.demo.src.feed.model.commentLike;

import com.example.demo.src.feed.entity.CommentLike;
import com.example.demo.src.feed.entity.FeedLike;
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
