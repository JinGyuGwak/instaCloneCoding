package com.example.demo.src.comment.dto;


import com.example.demo.src.comment.entity.CommentLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeDto {
    protected Long userId; //좋아요 한 유저 Id
    protected Long commentId;
    public CommentLikeDto(CommentLike commentLike){
        this.commentId=commentLike.getFeedComment().getId();
        this.userId=commentLike.getUser().getId();
    }
}
