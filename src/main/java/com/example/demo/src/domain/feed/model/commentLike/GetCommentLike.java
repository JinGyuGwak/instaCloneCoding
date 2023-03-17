package com.example.demo.src.domain.feed.model.commentLike;


import com.example.demo.src.entity.CommentLike;
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
