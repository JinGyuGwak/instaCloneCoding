package com.example.demo.src.feed.model.commentLike;

import com.example.demo.src.feed.entity.FeedComment;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostCommentLikeDto {

    private Long userId;
    private Long commentId;

    public PostCommentLikeDto(FeedComment feedComment){
        this.userId=feedComment.getUser().getId();
        this.commentId=feedComment.getId();
    }
}
