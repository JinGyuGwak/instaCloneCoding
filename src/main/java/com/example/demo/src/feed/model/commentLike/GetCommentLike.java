package com.example.demo.src.feed.model.commentLike;


import com.example.demo.src.feed.entity.CommentLike;
import com.example.demo.src.feed.entity.FeedLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCommentLike {
    private Long userId;

    public GetCommentLike(CommentLike commentLike){
        this.userId= commentLike.getUser().getId();
    }
}
